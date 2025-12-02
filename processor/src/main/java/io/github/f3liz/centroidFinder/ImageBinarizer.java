package io.github.f3liz.centroidFinder;

import java.awt.image.BufferedImage;

/**
 * An interface for converting between RGB images and binary (black-and-white) images.
 * A binary image is represented as a 2D array of integers, where each pixel is either 0 (black) or 1 (white).
 */
public interface ImageBinarizer {

    /**
     * Converts the given BufferedImage into a binary 2D array.
     * Each entry in the returned array is either 0 or 1, representing a black or white pixel.
     *
     * @param image the input BufferedImage to convert
     * @return a 2D array of integers where 1 represents white and 0 represents black
     */
    public int[][] toBinaryArray(BufferedImage image);

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each element in the array should be either 0 (black) or 1 (white).
     * 
     * Black pixels should be represented as x000000 and white pixels should be
     * represented as xFFFFFF.
     *
     * @param image a 2D binary array where 1 represents white and 0 represents black
     * @return a BufferedImage representation of the binary array
     */
    public BufferedImage toBufferedImage(int[][] image);
}
