package io.github.f3liz.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.Color;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    @Test
    public void testToBinaryArray_Basic() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 255, 255).getRGB());
        image.setRGB(1, 0, new Color(200, 200, 200).getRGB());
        image.setRGB(0, 1, new Color(0, 0, 0).getRGB());
        image.setRGB(1, 1, new Color(50, 50, 50).getRGB());

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = imageBinarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1 },
                { 0, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_ThresholdBoundary() {
        BufferedImage image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(250, 250, 250).getRGB()); // very close to white
        image.setRGB(1, 0, new Color(200, 200, 200).getRGB()); // farther from white

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1 } // 250,250,250 and 200,200,200 are both close enough here
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_AllBlackImage() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                image.setRGB(x, y, new Color(0, 0, 0).getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 0, 0 },
                { 0, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_AllWhiteImage() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, new Color(255, 255, 255).getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 10);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1, 1 },
                { 1, 1, 1 },
                { 1, 1, 1 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_CustomTargetColor() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 0, 0).getRGB()); // Red
        image.setRGB(1, 0, new Color(0, 255, 0).getRGB()); // Green
        image.setRGB(0, 1, new Color(0, 0, 255).getRGB()); // Blue
        image.setRGB(1, 1, new Color(255, 0, 10).getRGB()); // Slightly different red

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 50);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 0 },
                { 0, 1 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_OddlySizesImage() {
        BufferedImage image = new BufferedImage(5, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 5; x++) {
                image.setRGB(x, y, (x + y) % 2 == 0 ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 10);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 0, 1, 0, 1 },
                { 0, 1, 0, 1, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBufferedImage_Basic() {
        int[][] image = {
                { 1, 0 },
                { 0, 1 }
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int white = 0xFFFFFF;
        int black = 0x000000;

        assertEquals(white, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(white, actual.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
public void testToBufferedImage_LargerImage() {
    int[][] image = {
        {1, 0, 1, 0},
        {0, 1, 0, 1},
        {1, 0, 1, 0},
        {0, 1, 0, 1}
    };

    ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
    DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

    BufferedImage actual = imageBinarizer.toBufferedImage(image);

    int white = 0xFFFFFF;
    int black = 0x000000;

    // Test the middle pixels and edge pixels to ensure they are set correctly
    assertEquals(white, actual.getRGB(0, 0) & 0xFFFFFF);  // Top-left pixel (should be white)
    assertEquals(black, actual.getRGB(1, 0) & 0xFFFFFF);  // Top-right pixel (should be black)
    assertEquals(black, actual.getRGB(0, 1) & 0xFFFFFF);  // Bottom-left pixel (should be black)
    assertEquals(white, actual.getRGB(1, 1) & 0xFFFFFF);  // Bottom-right pixel (should be white)

    // Middle pixels
    assertEquals(white, actual.getRGB(2, 2) & 0xFFFFFF);  // Third row, third column (should be white)
    assertEquals(black, actual.getRGB(3, 2) & 0xFFFFFF);  // Third row, fourth column (should be black)

    // Check other pixels to make sure the whole image is as expected
    assertEquals(white, actual.getRGB(0, 2) & 0xFFFFFF);  // Third row, first column (should be white)
    assertEquals(black, actual.getRGB(1, 2) & 0xFFFFFF); 
}
    @Test
    public void testToBufferedImage_AllWhite() {
        int[][] image = {
                { 1, 1 },
                { 1, 1 }
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int white = 0xFFFFFF;

        // Check all pixels are white
        assertEquals(white, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(white, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(white, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(white, actual.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
    public void testToBufferedImage_AllBlack() {
        int[][] image = {
                { 0, 0 },
                { 0, 0 }
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int black = 0x000000;

        // Check all pixels are black
        assertEquals(black, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
    public void testToBufferedImage_SinglePixel() {
        int[][] image = {
                { 1 }
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int white = 0xFFFFFF;

        // Check that the single pixel is white
        assertEquals(white, actual.getRGB(0, 0) & 0xFFFFFF);
    }

    @Test
    public void testToBufferedImage_AlternatingPattern() {
        int[][] image = {
                { 1, 0, 1, 0 },
                { 0, 1, 0, 1 },
                { 1, 0, 1, 0 },
                { 0, 1, 0, 1 }
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int white = 0xFFFFFF;
        int black = 0x000000;

        // Check alternating pattern across all pixels
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if ((x + y) % 2 == 0) {
                    assertEquals(white, actual.getRGB(x, y) & 0xFFFFFF);
                } else {
                    assertEquals(black, actual.getRGB(x, y) & 0xFFFFFF);
                }
            }
        }
    }
}