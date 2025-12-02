package io.github.f3liz.centroidFinder;

/**
 * Command-line tool to process each frame of an MP4 video file to find the
 * largest centroid and write
 * to a CSV that centroids x and y coordinates by frame
 */
public class VideoSummaryApp {
    public static void main(String[] args) {
        // Logic to make sure only 4 arguments are given
        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
            return;
        }

        long startTime = System.currentTimeMillis(); // Start timer

        // Take in and parse the command line arguments
        String inputPath = args[0];
        String outputCsv = args[1];
        int targetColor = Integer.parseInt(args[2], 16);
        int threshold = Integer.parseInt(args[3]);

        try {
            VideoProcessor processor = new VideoProcessor(inputPath, outputCsv, targetColor, threshold);

            processor.processVideo();
            System.out.println("Processing complete, saved to: " + outputCsv);

            // Print total elapsed time
            long endTime = System.currentTimeMillis();
            double elapsedSeconds = (endTime - startTime) / 1000.0;
            System.out.println("Elapsed time: " + elapsedSeconds + " seconds");
            System.out.flush();

        } catch (Exception e) {
            System.out.println("Error processing video: ");
            e.printStackTrace();
        }
    }
}
