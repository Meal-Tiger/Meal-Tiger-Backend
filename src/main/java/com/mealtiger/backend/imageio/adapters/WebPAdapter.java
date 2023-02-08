package com.mealtiger.backend.imageio.adapters;

import com.mealtiger.backend.configuration.Configurator;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This is a Java class which converts BufferedImages to WebP.
 *
 * @author Sebastian Maier, Lucca Greschner, Kay Knöpfle
 */
public class WebPAdapter implements ImageAdapter {

    @Override
    public byte[] convert(BufferedImage input) throws IllegalStateException, IllegalArgumentException, IOException {
        ImmutableImage image = ImmutableImage.fromAwt(input);

        Configurator configurator = new Configurator();
        String compressionType = configurator.getString("Image.WebP.compressionType");

        if ("DEFAULT".equals(compressionType)) {
            return image.bytes(WebpWriter.DEFAULT);
        } else if ("LOSSLESS".equals(compressionType)) {
            return image.bytes(WebpWriter.MAX_LOSSLESS_COMPRESSION);
        } else if ("CUSTOM".equals(compressionType)) {
            int compressionFactor = configurator.getInteger("Image.WebP.compressionFactor");
            int compressionMethod = configurator.getInteger("Image.WebP.compressionMethod");
            return image.bytes(WebpWriter.DEFAULT
                    .withQ(compressionFactor)
                    .withM(compressionMethod)
            );
        } else if ("CUSTOM_LOSSLESS".equals(compressionType)) {
            int compressionFactor = configurator.getInteger("Image.WebP.compressionFactor");
            int compressionMethod = configurator.getInteger("Image.WebP.compressionMethod");
            int losslessSpeedFactor = configurator.getInteger("Image.WebP.losslessSpeedFactor");
            return image.bytes(WebpWriter.MAX_LOSSLESS_COMPRESSION
                    .withZ(losslessSpeedFactor)
                    .withQ(compressionFactor)
                    .withM(compressionMethod)
            );
        }
        else {
            throw new IllegalArgumentException("No such compressionType as " + compressionType + "!");
        }
    }
}
