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
 * This is a Java class which converts BufferedImages to GIF.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
public class GIFAdapter implements ImageAdapter{
    private final ImageWriter imageWriter;
    private final ImageWriteParam params;

    public GIFAdapter() {
        imageWriter = ImageIO.getImageWritersByFormatName("gif").next();

        Configurator configurator = new Configurator();
        double compressionQuality = configurator.getDouble("Image.GIF.compressionQuality");

        if (compressionQuality > 100 || compressionQuality <= 0){
            throw new InvalidConfigPropertyException("Image.GIF.compressionQuality",
                    "Compression quality cannot be higher than 100 or lower than 1! Current value is " + compressionQuality);
        }

        params = imageWriter.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionType("LZW");
        params.setCompressionQuality(((float) compressionQuality)/100);
    }

    @Override
    public byte[] convert(BufferedImage input) throws IllegalStateException, IllegalArgumentException, IOException{
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);
            imageWriter.write(null, new IIOImage(input, null, null), params);
            imageOutputStream.close();
            return outputStream.toByteArray();
        }
    }
}

