package io.github.f3liz.centroidFinder;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class FrameToBufferedImageConverterTest {

    @Test
    public void testConvert_ValidFrame_ReturnsBufferedImage() {
        // Arrange
        BufferedImage inputImage = new BufferedImage(100, 50, BufferedImage.TYPE_3BYTE_BGR);
        Java2DFrameConverter refConverter = new Java2DFrameConverter();
        Frame testFrame = refConverter.convert(inputImage);

        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();

        // Act
        BufferedImage result = converter.convert(testFrame);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getWidth());
        assertEquals(50, result.getHeight());
    }

    @Test
    public void testConvert_NullInput_ReturnsNull() {
        // Arrange
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();

        // Act
        BufferedImage result = converter.convert(null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testConvert_FrameFromARGBImage_PreservesSize() {
        // Arrange
        BufferedImage argbImage = new BufferedImage(80, 60, BufferedImage.TYPE_INT_ARGB);
        Java2DFrameConverter refConverter = new Java2DFrameConverter();
        Frame testFrame = refConverter.convert(argbImage);

        // Act
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();
        BufferedImage result = converter.convert(testFrame);

        // Assert
        assertNotNull(result);
        assertEquals(80, result.getWidth());
        assertEquals(60, result.getHeight());
    }

    @Test
    public void testConvert_GrayscaleImage() {
        // Arrange
        BufferedImage grayImage = new BufferedImage(120, 90, BufferedImage.TYPE_BYTE_GRAY);
        Java2DFrameConverter refConverter = new Java2DFrameConverter();
        Frame testFrame = refConverter.convert(grayImage);

        // Act
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();
        BufferedImage result = converter.convert(testFrame);

        // Assert
        assertNotNull(result);
        assertEquals(120, result.getWidth());
        assertEquals(90, result.getHeight());
    }

    @Test
    public void testConvert_LargeImage() {
        // Arrange
        BufferedImage largeImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_3BYTE_BGR);
        Java2DFrameConverter refConverter = new Java2DFrameConverter();
        Frame testFrame = refConverter.convert(largeImage);

        // Act
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();
        BufferedImage result = converter.convert(testFrame);

        // Assert
        assertNotNull(result);
        assertEquals(1920, result.getWidth());
        assertEquals(1080, result.getHeight());
    }

    @Test
    public void testConvert_MultipleFrames() {
        // Arrange
        BufferedImage image1 = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        BufferedImage image2 = new BufferedImage(60, 60, BufferedImage.TYPE_INT_RGB);

        Java2DFrameConverter refConverter = new Java2DFrameConverter();
        Frame frame1 = refConverter.convert(image1);
        Frame frame2 = refConverter.convert(image2);

        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();

        // Act
        BufferedImage result1 = converter.convert(frame1);
        BufferedImage result2 = converter.convert(frame2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(50, result1.getWidth());
        assertEquals(60, result2.getWidth());
    }

}
