package com.mealtiger.backend.imageio.adapters;

import java.awt.image.BufferedImage;

/**
 * This class is a factory which provides Adapters that convert images into desired types.
 */
public class ImageAdapterFactory {

    private final ImageAdapter outputAdapter;

    public ImageAdapterFactory(String format, BufferedImage input) {
        ImageAdapter chosenAdapter;

        switch (format) {
            case "jpg" -> chosenAdapter = new JPEGAdapter(input);
            case "png" -> chosenAdapter = new PNGAdapter(input);
            case "webp" -> chosenAdapter = new WebPAdapter(input);
            case "gif" -> chosenAdapter = new GIFAdapter(input);
            case "bmp" -> chosenAdapter = new BitmapAdapter(input);
            default -> throw new IllegalArgumentException(format + " is not a legal option for ImageAdapterFactory!");
        }

        outputAdapter = chosenAdapter;
    }

    public ImageAdapter getInstance() {
        return outputAdapter;
    }
}
