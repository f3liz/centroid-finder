package io.github.f3liz.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

/**
 * Class contains method/logic to process one frame
 * per second of an MP4 video file to find the largest centroid and write
 * to a CSV that contains centroids' x and y coordinates by time
 */
public class VideoProcessor {
    private final String inputPath;
    private final String outputCsv;
    private final int targetColor;
    private final int threshold;

    public VideoProcessor(String inputPath, String outputCsv, int targetColor, int threshold) {
        this.inputPath = inputPath;
        this.outputCsv = outputCsv;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    // Main logic for processing video and writing centroid coordinates to CSV
    public void processVideo() throws Exception {
        // Create the DistanceImageBinarizer with a EuclideanColorDistance instance.
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Set up the logic to find largest group
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        // Conversion for Frame to BufferedImage
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();

        // Grabber to read frames and writer to write to the output CSV
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File(inputPath));
             PrintWriter writer = new PrintWriter(outputCsv)) {

            grabber.start();

            // Write header line in required format
            writer.println("time,x,y");

            double frameRate = grabber.getFrameRate();
            double durationSeconds = grabber.getLengthInTime() / 1_000_000.0; // microseconds to seconds

            System.out.println("Video duration: " + durationSeconds + " seconds");
            System.out.println("Frame rate: " + frameRate + " fps");

            // Gets rid of pixel warning in terminal
            org.bytedeco.ffmpeg.global.avutil.av_log_set_level(org.bytedeco.ffmpeg.global.avutil.AV_LOG_ERROR);

            // Process one frame per second
            for (int sec = 0; sec < (int) durationSeconds; sec++) {
                // Set grabber to correct timestamp (in microseconds)
                grabber.setTimestamp(sec * 1_000_000L); // 1 second = 1,000,000 µs

                Frame frame = grabber.grabImage();
                if (frame == null) continue; // skip if no frame at that timestamp

                BufferedImage image = converter.convert(frame);
                List<Group> groups = groupFinder.findConnectedGroups(image);

                int xCoord = -1;
                int yCoord = -1;

                // Only update coordinates if a group was found
                if (!groups.isEmpty()) {
                    Group biggest = groups.get(0);
                    xCoord = biggest.centroid().x();
                    yCoord = biggest.centroid().y();
                }

                // Write the time and coordinates to CSV
                writer.printf("%d,%d,%d%n", sec, xCoord, yCoord);

                System.out.println("Processed second " + sec + " (timestamp: " + (sec) + "s)");
            }

            grabber.stop();
        }
    }
}