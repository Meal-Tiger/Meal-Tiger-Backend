package com.mealtiger.backend.imageio;

import com.mealtiger.backend.imageio.adapters.*;
import org.springframework.beans.factory.FactoryBean;

import java.util.Objects;

/**
 * This class is a factory bean which provides Adapters that convert images into desired types.
 */
public class ImageAdapterFactory implements FactoryBean<ImageAdapter> {

    private final String format;

    ImageAdapterFactory(String format) {
        this.format = format;
    }

    @Override
    public ImageAdapter getObject() {
        ImageAdapter chosenAdapter;

        switch (format) {
            case "jpeg" -> chosenAdapter = new JPEGAdapter();
            case "png" -> chosenAdapter = new PNGAdapter();
            case "webp" -> chosenAdapter = new WebPAdapter();
            case "gif" -> chosenAdapter = new GIFAdapter();
            case "bmp" -> chosenAdapter = new BitmapAdapter();
            default -> throw new IllegalArgumentException(format + " is not a legal option for ImageAdapterFactory!");
        }

        return chosenAdapter;
    }

    @Override
    public Class<?> getObjectType() {
        return Objects.requireNonNull(getObject()).getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
