package com.mealtiger.backend.imageio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ImageSource {

    static Stream<BufferedImage> bitmapImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.bmp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.bmp")))
        );
    }

    static Stream<BufferedImage> jpegImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.jpg"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.jpg"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.jpg")))
        );
    }

    static Stream<BufferedImage> pngImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.png"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.png")))
        );
    }

    static Stream<BufferedImage> gifImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.gif"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.gif")))
        );
    }

    static Stream<BufferedImage> lossyWebPImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.webp")))
        );
    }

    static Stream<BufferedImage> losslessWebPImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.lossless.webp"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.lossless.webp")))
        );
    }

    static Stream<BufferedImage> tiffImageStream() throws IOException {
        return Stream.of(
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/Flower/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/PiggyBank/TestImage.tiff"))),
                ImageIO.read(Objects.requireNonNull(ImageSource.class.getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/TransparentTestImage/TestImage.tiff")))
        );
    }

}
