package io.github.f3liz.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class EuclideanColorDistanceTest {
    
    @Test
    public void testConvertHexToRGB_Black() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {0, 0, 0}, distance.convertHexToRGB(0x000000));
    }

    @Test
    public void testConvertHexToRGB_White() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {255, 255, 255}, distance.convertHexToRGB(0xFFFFFF));
    }

    @Test
    public void testConvertHexToRGB_Yellow() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {255, 255, 0}, distance.convertHexToRGB(0xFFFF00));
    }

    @Test
    public void testConvertHexToRGB_Blue() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {0, 0, 255}, distance.convertHexToRGB(0x0000FF));
    }

    @Test
    public void testConvertHexToRGB_RandomColor() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {18, 52, 86}, distance.convertHexToRGB(0x123456));
    }

    @Test
    public void testDistance_BlackAndBlack() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int black = 0x000000;
        double actual = distance.distance(black, black);
        assertEquals(0.0, actual);
    }

    @Test
    public void testDistance_RedToGreen() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int red = 0xFF0000;
        int green = 0x00FF00;
        // Distance between red and green
        // sqrt((255-0)^2 + (0-255)^2 + (0-0)^2)
        // = sqrt(65025 + 65025 + 0) = sqrt(130050) ≈ 360.62
        assertEquals(360.62, distance.distance(red, green), 0.01);
    }

    @Test
    public void testDistance_RedToBlue() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int red = 0xFF0000;
        int blue = 0x0000FF;
        // Distance between red and blue
        // sqrt((255-0)^2 + (0-0)^2 + (0-255)^2)
        // = sqrt(65025 + 0 + 65025) = sqrt(130050) ≈ 360.62
        assertEquals(360.62, distance.distance(red, blue), 0.01);
    }

    @Test
    public void testDistance_BlackToWhite() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int black = 0x000000;
        int white = 0xFFFFFF;
        // Distance between black and white
        // sqrt((0-255)^2 + (0-255)^2 + (0-255)^2)
        // = sqrt(65025 + 65025 + 65025) = sqrt(195075) ≈ 441.67
        assertEquals(441.67, distance.distance(black, white), 0.01);
    }

    @Test
    public void testDistance_RandomColors() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int colorA = 0x123456; // A medium color
        int colorB = 0x654321; // Another medium color
        
        // Distance between the random colors
        // sqrt((18-101)^2 + (52-67)^2 + (86-33)^2)
        // = sqrt(6889 + 225 + 2809) = sqrt(9923) ≈ 99.61
        assertEquals(99.61, distance.distance(colorA, colorB), 0.01);
    }

    @Test
    public void testDistance_SmallDifference() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int colorA = 0x123456;
        int colorB = 0x123457;  // Only a 1-unit difference in the last component
        // This should return a very small distance
        // sqrt((18-18)^2 + (52-52)^2 + (86-87)^2) = sqrt(0 + 0 + 1) = 1.0
        assertEquals(1.0, distance.distance(colorA, colorB), 0.0001);
    }

    @Test
    public void testDistance_MaxColor() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int colorA = 0xFFFFFF; // White
        int colorB = 0x000000; // Black
        // Distance between black and white should be maximum
        assertEquals(441.67, distance.distance(colorA, colorB), 0.01);
    }

}
