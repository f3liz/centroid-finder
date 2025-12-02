package io.github.f3liz.centroidFinder;

/**
 * Defines an interface for computing the distance between two colors.
 * 
 * Colors are typically represented as 24-bit RGB integers in the format 0xRRGGBB, where each pair of hexadecimal
 * digits corresponds to the red, green, and blue components of the color.
 * Implementations of this interface should provide a method to compute a numeric distance between two colors.
 */
public interface ColorDistanceFinder {
    /**
     * Computes the distance between two colors.
     *
     * Each color is represented as a 24-bit integer (0xRRGGBB) where the red, green, and blue components can be
     * extracted using bit shifting and masking.
     *
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the computed distance between the two colors
     */
    public double distance(int colorA, int colorB);
}
