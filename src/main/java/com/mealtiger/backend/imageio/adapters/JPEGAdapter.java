package com.mealtiger.backend.imageio.adapters;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.configuration.exceptions.InvalidConfigPropertyException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This is a Java class which converts BufferedImages to JPEG.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
public class JPEGAdapter implements ImageAdapter {


    private final BufferedImage input;
    private final ImageWriter imageWriter;
    private final ImageWriteParam params;

    JPEGAdapter(BufferedImage image) {
        if (image.getColorModel().hasAlpha()) {
            // Remove alpha channel
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = newImage.createGraphics();
            graphics2D.fillRect(0,0, image.getWidth(), image.getHeight());
            graphics2D.drawImage(image, 0, 0, null);
            graphics2D.dispose();

            input = newImage;
        } else {
            input = image;
        }

        imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();

        Configurator configurator = new Configurator();
        double compressionQuality = configurator.getDouble("Image.JPEG.compressionQuality");

        if (compressionQuality > 100 || compressionQuality <= 0) {
            throw new InvalidConfigPropertyException("Image.JPEG.compressionQuality",
                    "Compression quality cannot be higher than 100 or lower than 1! Current value is " + compressionQuality);
        }

        params = imageWriter.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(((float) compressionQuality)/100);
    }

    public byte[] convert() throws IllegalStateException, IllegalArgumentException, IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
            imageWriter.setOutput(imageOutputStream);
            imageWriter.write(null, new IIOImage(input, null, null), params);
            return outputStream.toByteArray();
        }
    }
}