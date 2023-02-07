package com.mealtiger.backend.imageio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ImageSource {

    /**
     * Provides a stream of bitmap test images.
     * @return Stream containing BufferedImages of bitmap test images.
     */
    static Stream<BufferedImage> bitmapImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.bmp")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of jpeg test images.
     * @return Stream containing BufferedImages of jpeg test images.
     */
    static Stream<BufferedImage> jpegImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.jpg"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.jpg"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.jpg")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of png test images.
     * @return Stream containing BufferedImages of png test images.
     */
    static Stream<BufferedImage> pngImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.png")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of gif test images.
     * @return Stream containing BufferedImages of gif test images.
     */
    static Stream<BufferedImage> gifImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.gif")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of lossy webp test images.
     * @return Stream containing BufferedImages of lossy webp test images.
     */
    static Stream<BufferedImage> lossyWebPImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.webp")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of lossless webp test images.
     * @return Stream containing BufferedImages of lossless webp test images.
     */
    static Stream<BufferedImage> losslessWebPImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.lossless.webp")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

    /**
     * Provides a stream of tiff test images.
     * @return Stream containing BufferedImages of tiff test images.
     */
    static Stream<BufferedImage> tiffImageStream() throws IOException {
        ImageIO.setUseCache(false);
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.tiff")))
        ).map(image -> {
            BufferedImage indexedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
            Graphics2D graphics2D = indexedImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, null);
            return indexedImage;
        });
    }

}
