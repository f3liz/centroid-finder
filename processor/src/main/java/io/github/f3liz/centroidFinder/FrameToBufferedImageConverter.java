package io.github.f3liz.centroidFinder;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;

/**
 * Utility class for converting a JavaCV Frame object into a standard Java BufferedImage.
 * 
 * This is to be used with the other centroid-finder classes to convert frames into BufferedImage's
 * to complete centroid finding algorithms.
 */
public class FrameToBufferedImageConverter {
    private final Java2DFrameConverter converter;

    /**
     * Constructs a new instance of FrameToBufferedImageConverter using the JavaCV 
     * Java2DFrameConverter method.
     */
    public FrameToBufferedImageConverter() {
        this.converter = new Java2DFrameConverter();
    }

    /**
     * Converts a JavaCV Frame into a BufferedImage
     * 
     * @param frame the inputted frame to be converted
     * @return a BufferedImage representation of the passed in Frame
     */
    public BufferedImage convert (Frame frame) {
        return converter.convert(frame);
    }
}
