package com.mealtiger.backend.imageio.adapters;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.configuration.exceptions.InvalidConfigPropertyException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This is a Java class which converts BufferedImages to PNG.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
public class PNGAdapter implements ImageAdapter {

    private final BufferedImage input;
    private final ImageWriter imageWriter;
    private final ImageWriteParam params;

    PNGAdapter(BufferedImage image) {
        input = image;
        imageWriter = ImageIO.getImageWritersByFormatName("png").next();


        Configurator configurator = new Configurator();
        double compressionQuality = configurator.getDouble("Image.PNG.compressionQuality");

        if (compressionQuality > 100 || compressionQuality <= 0) {
            throw new InvalidConfigPropertyException("Image.PNG.compressionQuality",
                    "Compression quality cannot be higher than 100 or lower than 1! Current value is " + compressionQuality);
        }

        params = imageWriter.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(((float) compressionQuality) / 100);
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
