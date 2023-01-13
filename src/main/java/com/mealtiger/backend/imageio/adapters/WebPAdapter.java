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
        PNGAdapter pngAdapter = new PNGAdapter();
        ImmutableImage image = ImmutableImage.loader().fromBytes(pngAdapter.convert(input));

        Configurator configurator = new Configurator();
        String compressionType = configurator.getString("Image.WebP.compressionType");

        if ("DEFAULT".equals(compressionType)) {
            return image.bytes(WebpWriter.DEFAULT);
        }

        return image.bytes(WebpWriter.MAX_LOSSLESS_COMPRESSION);
    }
}
