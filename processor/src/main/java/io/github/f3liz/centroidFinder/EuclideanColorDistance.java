package io.github.f3liz.centroidFinder;

public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {
        // Converts each hex color into an integer array of their RGB components using convertHexToRGB
        int[] colorARGB = convertHexToRGB(colorA);
        int[] colorBRGB = convertHexToRGB(colorB);

        // Calculating the differences for each Red, Green, and Blue color component
        int redDifference = colorARGB[0] - colorBRGB[0];
        int greenDifference = colorARGB[1] - colorBRGB[1];
        int blueDifference = colorARGB[2] - colorBRGB[2];

        // Calculate and return the Euclidean distance between the two colors
        return Math.sqrt((redDifference * redDifference) + (greenDifference * greenDifference) + (blueDifference * blueDifference));
    }

    /**
     * Converts the passed in integer hex color in the form (0xRRGGBB) to an integer array of Red, Green, and Blue values.
     * 
     * @param color the color as a 24-bit hex RGB integer
     * @return an array containing the red, green, and blue values of color
     */
    public int[] convertHexToRGB(int color) {
        // Extract red component by shifting 16 bits to the right and masking with 0xff
        int red = (color >> 16) & 0xff;

        // Extract green component by shifting 8 bits to the right and masking with 0xff
        int green = (color >> 8) & 0xff;

        // Extract blue component by masking with 0xff to get the last 8 bits
        int blue = color & 0xff;

        // Returns the integer array of RGB values
        return new int[] {red, green, blue};
    }
}
