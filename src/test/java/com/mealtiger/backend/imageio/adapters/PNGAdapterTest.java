package com.mealtiger.backend.imageio.adapters;

import com.mealtiger.backend.UnitTestConfigSetup;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test tests the class PNGAdapter.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@Tag("unit")
class PNGAdapterTest {

    private ImageAdapter pngAdapter;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        pngAdapter = new PNGAdapter();
    }

    @BeforeAll
    static void beforeAll() {
        UnitTestConfigSetup.setupConfigs();
    }

    @AfterAll
    static void afterAll() {
        UnitTestConfigSetup.teardownConfigs();
    }

    /**
     * Tests conversion of bitmap images
     * @param image Bitmap image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#bitmapImageStream")
    void bitmapConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of jpeg images
     * @param image JPEG image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#jpegImageStream")
    void jpegConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of png images
     * @param image PNG image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#pngImageStream")
    void pngConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of gif images
     * @param image GIF image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#gifImageStream")
    void gifConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of lossy webp images
     * @param image WebP image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#lossyWebPImageStream")
    void lossyWebPConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of lossless webp images
     * @param image WebP image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#losslessWebPImageStream")
    void losslessWebPConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    /**
     * Tests conversion of tiff images
     * @param image TIFF image gathered from the method source.
     */
    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#tiffImageStream")
    void tiffConversionTest(BufferedImage image) throws IOException {
        byte[] output = pngAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("png", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }


}
