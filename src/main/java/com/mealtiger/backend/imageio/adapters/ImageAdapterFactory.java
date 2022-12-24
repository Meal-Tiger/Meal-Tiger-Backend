package com.mealtiger.backend.imageio.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This class is a factory which provides Adapters that convert images into desired types.
 */
public class ImageAdapterFactory {

    private static final Logger log = LoggerFactory.getLogger(ImageAdapterFactory.class);

    private final ImageAdapter outputAdapter;

    public ImageAdapterFactory(String format, BufferedImage input) {
        ImageAdapter chosenAdapter;
        try {
            switch (format) {
                case "jpg" -> chosenAdapter = new JPEGAdapter(input);
                case "png" -> chosenAdapter = new PNGAdapter(input);
                case "webp" -> chosenAdapter = new WebPAdapter(input);
                case "gif" -> chosenAdapter = new GIFAdapter(input);
                case "bmp" -> chosenAdapter = new BitmapAdapter(input);
                default -> throw new IllegalArgumentException(format + " is not a legal option for ImageAdapterFactory!");
            }
        } catch (IOException e) {
            // This should not happen
            chosenAdapter = null;
            log.error("IOException upon trying to convert posted image! Further information: {}", e.getMessage());
        }

        outputAdapter = chosenAdapter;
    }

    public ImageAdapter getInstance() {
        return outputAdapter;
    }
}
