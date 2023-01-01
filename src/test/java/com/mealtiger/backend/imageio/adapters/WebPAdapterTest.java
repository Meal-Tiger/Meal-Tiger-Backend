package com.mealtiger.backend.imageio.adapters;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WebPAdapterTest {

    @Autowired
    private ImageAdapter webPAdapter;

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#bitmapImageStream")
    void bitmapConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#jpegImageStream")
    void jpegConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#pngImageStream")
    void pngConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#gifImageStream")
    void gifConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#lossyWebPImageStream")
    void lossyWebPConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#losslessWebPImageStream")
    void losslessWebPConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }

    @ParameterizedTest
    @MethodSource("com.mealtiger.backend.imageio.ImageSource#tiffImageStream")
    void tiffConversionTest(BufferedImage image) throws IOException {
        byte[] output = webPAdapter.convert(image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(output)) {
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
            assertEquals("webp", ImageIO.getImageReaders(imageInputStream).next().getFormatName());
        }
    }


}
