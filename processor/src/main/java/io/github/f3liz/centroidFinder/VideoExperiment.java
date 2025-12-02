package io.github.f3liz.centroidFinder;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import javax.swing.*;

public class VideoExperiment {
    public static void main(String[] args) {
        // path to the video
        String videoPath = "sampleInput/sample-10s.mp4";

        // Create grabber objects from FFmpegFrameGrabber class from JavaCV
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);

        // makes new video frame
        CanvasFrame canvas = new CanvasFrame("Video Frame");
        // stops program if video window is closed
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // makes video fit on screen
        canvas.setCanvasSize(1280, 720);

        // centers on our screen
        canvas.setLocationRelativeTo(null);

        // frame counter
        int frameCount = 0;

        try {
            grabber.start();

            // You can also get metadata like this:
            System.out.println("Width: " + grabber.getImageWidth());
            System.out.println("Height: " + grabber.getImageHeight());
            System.out.println("Frame Rate: " + grabber.getFrameRate());
            System.out.println("Length in Frames: " + grabber.getLengthInFrames());

            Frame frame;

            while ((frame = grabber.grabImage()) != null) {
                frameCount++;
                canvas.showImage(frame);

                if (!canvas.isVisible()) break;

                Thread.sleep(30); // ~30 FPS display speed
                System.out.println("Frame " + frameCount + " processed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // closes video window
                grabber.stop();
                grabber.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvas.dispose();
            System.out.println("Processed " + frameCount + " frames.");
        }
    }
}