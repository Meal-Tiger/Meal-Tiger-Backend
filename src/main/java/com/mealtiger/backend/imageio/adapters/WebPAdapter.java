package com.mealtiger.backend.imageio.adapters;

import com.mealtiger.backend.configuration.Configurator;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This is a Java class which converts BufferedImages to WebP.
 *
 * @author Sebastian Maier, Lucca Greschner, Kay Knöpfle
 */
public class WebPAdapter implements ImageAdapter {
    private final BufferedImage input;

    WebPAdapter(BufferedImage image) throws IOException {
        input = image;
    }

    public byte[] convert() throws IllegalStateException, IllegalArgumentException, IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(input, "jpg", outputStream);

            ImmutableImage image = ImmutableImage.loader().fromBytes(outputStream.toByteArray());

            Configurator configurator = new Configurator();
            String compressionType = configurator.getString("Image.WebP.compressionType");

            if ("DEFAULT".equals(compressionType)) {
                return image.bytes(WebpWriter.DEFAULT);
            }

            return image.bytes(WebpWriter.MAX_LOSSLESS_COMPRESSION);
        }
    }
}
