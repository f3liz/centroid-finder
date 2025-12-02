package io.github.f3liz.centroidFinder;

import java.awt.image.BufferedImage;

/**
 * An implementation of the ImageBinarizer interface that uses color distance
 * to determine whether each pixel should be black or white in the binary image.
 * 
 * The binarization is based on the Euclidean distance between a pixel's color and a reference target color.
 * If the distance is less than the threshold, the pixel is considered white (1);
 * otherwise, it is considered black (0).
 * 
 * The color distance is computed using a provided ColorDistanceFinder, which defines how to compare two colors numerically.
 * The targetColor is represented as a 24-bit RGB integer in the form 0xRRGGBB.
 */
public class DistanceImageBinarizer implements ImageBinarizer {
    private final ColorDistanceFinder distanceFinder;
    private final int threshold;
    private final int targetColor;

    /**
     * Constructs a DistanceImageBinarizer using the given ColorDistanceFinder,
     * target color, and threshold.
     * 
     * The distanceFinder is used to compute the Euclidean distance between a pixel's color and the target color.
     * The targetColor is represented as a 24-bit hex RGB integer (0xRRGGBB).
     * The threshold determines the cutoff for binarization: pixels with distances less than
     * the threshold are marked white, and others are marked black.
     *
     * @param distanceFinder an object that computes the distance between two colors
     * @param targetColor the reference color as a 24-bit hex RGB integer (0xRRGGBB)
     * @param threshold the distance threshold used to decide whether a pixel is white or black
     */
    public DistanceImageBinarizer(ColorDistanceFinder distanceFinder, int targetColor, int threshold) {
        this.distanceFinder = distanceFinder;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    /**
     * Converts the given BufferedImage into a binary 2D array using color distance and a threshold.
     * Each entry in the returned array is either 0 or 1, representing a black or white pixel.
     * A pixel is white (1) if its Euclidean distance to the target color is less than the threshold.
     *
     * @param image the input RGB BufferedImage
     * @return a 2D binary array where 1 represents white and 0 represents black
     */
    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        // Get the height and width of the image
        int height = image.getHeight();
        int width = image.getWidth();

        // Create a 2D array to store the binary values of the image (0 for black, 1 for white)
        int[][] binaryImage = new int[height][width]; 

        // Iterating through each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB value of the current pixel and masking out alpha
                int currentRGB = image.getRGB(x, y) & 0xFFFFFF;

                // Calculate the Euclidean distance between the current pixel'sc color and the target color
                double currentDistance = distanceFinder.distance(currentRGB, targetColor);

                // Checks if the pixel's color is within the threshold, setting it to white (1), otherwise black (0)
                if (threshold >= currentDistance) {
                    binaryImage[y][x] = 1;
                } else {
                    binaryImage[y][x] = 0;
                }
            }
        }
        return binaryImage;
    }

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each value should be 0 (black) or 1 (white).
     * Black pixels are encoded as 0x000000 and white pixels as 0xFFFFFF.
     *
     * @param image a 2D array of 0s and 1s representing the binary image
     * @return a BufferedImage where black and white pixels are represented with standard RGB hex values
     */
    @Override
    public BufferedImage toBufferedImage(int[][] image) {
        // Get the height and width of the binary image array
        int height = image.length;
        int width = image[0].length;

        // Create a new BufferedImage of the same size as the binary image array
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Iterating through the binary image array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Setting the pixel to white if the binary value is 1, otherwise black for each corresponding pixel in the BufferedImage
                if (image[y][x] == 1) {
                    bufferedImage.setRGB(x, y, 0xFFFFFF);
                } else {
                    bufferedImage.setRGB(x, y, 0x000000);
                }
            }
        }
        return bufferedImage;
    }
}