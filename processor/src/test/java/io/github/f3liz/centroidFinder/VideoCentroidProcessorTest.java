package io.github.f3liz.centroidFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.Color;

import org.junit.jupiter.api.Test;

public class VideoCentroidProcessorTest {
    @Test
    public void testCentroidFromSingleColoredRegion() {
        // Arrange
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Draw a red square from (10,10) to (19,19)
        for (int x = 10; x < 20; x++) {
            for (int y = 10; y < 20; y++) {
                image.setRGB(x, y, new Color(255, 0, 0).getRGB()); // Red
            }
        }

        // Using raw BufferedImage directly to avoid issues from Frame conversion during
        // testing
        BufferedImage converted = image;

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 200);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        // Act
        List<Group> groups = groupFinder.findConnectedGroups(converted);

        // Assert
        assertFalse(groups.isEmpty());
        Group largest = groups.get(0);
        assertEquals(14, largest.centroid().x(), "Expected x of centroid");
        assertEquals(14, largest.centroid().y(), "Expected y of centroid");
    }

    @Test
    public void testNoMatchingColorProducesNegativeCoordinates() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        // Fill with blue only
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                image.setRGB(x, y, 0x0000FF);
            }
        }

        // Using raw BufferedImage directly to avoid issues from Frame conversion during
        // testing
        BufferedImage converted = image;

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 10); // red
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        List<Group> groups = groupFinder.findConnectedGroups(converted);

        assertTrue(groups.isEmpty());
    }

    @Test
    public void testChoosesLargestRedCentroid() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        // Draw small red square at (10,10) to (14,14)
        for (int x = 10; x < 15; x++) {
            for (int y = 10; y < 15; y++) {
                image.setRGB(x, y, new Color(255, 0, 0).getRGB());
            }
        }

        // Draw larger red square at (60,60) to (75,75)
        for (int x = 60; x < 76; x++) {
            for (int y = 60; y < 76; y++) {
                image.setRGB(x, y, new Color(255, 0, 0).getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 200);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        List<Group> groups = groupFinder.findConnectedGroups(image);

        assertFalse(groups.isEmpty());

        Group largest = groups.get(0);
        // The center of the larger square at (60,60)-(75,75) is approx (67,67)
        assertEquals(67, largest.centroid().x(), 1);
        assertEquals(67, largest.centroid().y(), 1);
    }

    @Test
    public void testSingleRedPixelCentroid() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        // Set one red pixel at (25, 25)
        image.setRGB(25, 25, new Color(255, 0, 0).getRGB());

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 10);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        List<Group> groups = groupFinder.findConnectedGroups(image);

        assertFalse(groups.isEmpty());
        Group only = groups.get(0);
        assertEquals(25, only.centroid().x());
        assertEquals(25, only.centroid().y());
    }

    @Test
    public void testNearMatchColorWithLowThresholdFails() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
        Color almostRed = new Color(250, 10, 10); // slightly off from pure red
        image.setRGB(15, 15, almostRed.getRGB());

        // Use a low threshold (should not match)
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 10);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        List<Group> groups = groupFinder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testNearMatchColorWithHighThresholdSucceeds() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
        Color almostRed = new Color(250, 10, 10); // slightly off from pure red
        image.setRGB(15, 15, almostRed.getRGB());

        // Use a high threshold (should match)
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 200);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        List<Group> groups = groupFinder.findConnectedGroups(image);
        assertFalse(groups.isEmpty());
    }
}