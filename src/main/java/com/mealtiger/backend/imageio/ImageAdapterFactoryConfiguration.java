package com.mealtiger.backend.imageio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This bean configures all the options for ImageAdapterFactory
 */
@Configuration
public class ImageAdapterFactoryConfiguration {

    @Bean(name = "bitmapAdapter")
    public ImageAdapterFactory bitmapAdapterFactory() {
        return new ImageAdapterFactory("bmp");
    }

    @Bean(name = "gifAdapter")
    public ImageAdapterFactory gifAdapterFactory() {
        return new ImageAdapterFactory("gif");
    }

    @Bean(name = "jpegAdapter")
    public ImageAdapterFactory jpegAdapterFactory() {
        return new ImageAdapterFactory("jpeg");
    }

    @Bean(name = "pngAdapter")
    public ImageAdapterFactory pngAdapterFactory() {
        return new ImageAdapterFactory("png");
    }

    @Bean(name = "webPAdapter")
    public ImageAdapterFactory webpAdapterFactory() {
        return new ImageAdapterFactory("webp");
    }

}
