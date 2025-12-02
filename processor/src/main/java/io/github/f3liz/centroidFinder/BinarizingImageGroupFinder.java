package io.github.f3liz.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * An implementation of the ImageGroupFinder interface that first binarizes a given image and then finds
 * connected groups of white pixels using a BinaryGroupFinder.
 * 
 * This class uses an ImageBinarizer to convert an RGB image into a binary 2D array (where each pixel is either 0 or 1).
 * The BinaryGroupFinder is then applied to the binary array to locate connected groups of white pixels.
 * The connected groups are returned sorted in descending order based on the criteria defined in the Group record.
 */
public class BinarizingImageGroupFinder implements ImageGroupFinder {
    private final ImageBinarizer binarizer;
    private final BinaryGroupFinder groupFinder;

    /**
     * Constructs a BinarizingImageGroupFinder using the specified ImageBinarizer and BinaryGroupFinder.
     *
     * @param binarizer the ImageBinarizer used to convert an image into a binary 2D array
     * @param groupFinder the BinaryGroupFinder used to find connected white pixel groups in the binary array
     */
    public BinarizingImageGroupFinder(ImageBinarizer binarizer, BinaryGroupFinder groupFinder) {
        this.binarizer = binarizer;
        this.groupFinder = groupFinder;
    }

    /**
     * Finds connected groups of white pixels in the given image.
     * 
     * The method first converts the input BufferedImage into a binary array using the ImageBinarizer.
     * White pixels are represented by 1 and black pixels by 0. It then uses the BinaryGroupFinder to
     * locate connected groups (neighbors connected vertically and horizontally) within the binary array.
     * The identified groups are returned in descending order, according to the sorting defined in the Group record.
     *
     * @param image the input BufferedImage to process
     * @return a list of groups representing connected white pixels in the image
     */
    @Override
    public List<Group> findConnectedGroups(BufferedImage image) {
        // Convert the RGB image into a binary array (0 for black, 1 for white) using the ImageBinarizer
        int[][] binaryImage = binarizer.toBinaryArray(image);

        // Use the BinaryGroupFinder to find connected groups of white (1) pixels in the binary image array
        List<Group> connectedGroups = groupFinder.findConnectedGroups(binaryImage);

        // Return the list of connected groups
        return connectedGroups;
    }
}
