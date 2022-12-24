package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;
/**
 * Image Config File.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 */
@SuppressWarnings("unused")
@Config(name = "Image", configPath = "image.yml")
public class ImageConfig {

    private final String imagePath;
    
    private final PNG png;
    private final JPEG jpeg;
    private final GIF gif;
    private final BMP bmp;
    private final WebP webp;

    public ImageConfig() {
        png = new PNG();
        jpeg = new JPEG();
        gif = new GIF();
        bmp = new BMP();
        webp = new WebP();
        imagePath = "images/";
    }

    @ConfigNode(name = "servedImageFormats")
    public String getServedImageFormats() {
        StringBuilder stringBuilder = new StringBuilder();
        if (png.enabled) {
            stringBuilder.append("png,");
        }
        if (jpeg.enabled) {
            stringBuilder.append("jpg,");
        }
        if (gif.enabled) {
            stringBuilder.append("gif,");
        }
        if (bmp.enabled) {
            stringBuilder.append("bmp,");
        }
        if (webp.enabled) {
            stringBuilder.append("webp,");
        }
        return stringBuilder.toString();
    }

    @ConfigNode(name = "PNG.compressionQuality")
    public double getPNGCompressionQuality() {
        return png.compressionQuality;
    }

    @ConfigNode(name = "JPEG.compressionQuality")
    public double getJPEGCompressionQuality() {
        return jpeg.compressionQuality;
    }

    @ConfigNode(name = "GIF.compressionQuality")
    public double getGIFCompressionQuality() {
        return gif.compressionQuality;
    }

    @ConfigNode(name = "BMP.compressionQuality")
    public double getBMPCompressionQuality() {
        return bmp.compressionQuality;
    }

    @ConfigNode(name = "BMP.compressionType")
    public String getBMPCompressionType() {
        return bmp.compressionType;
    }

    @ConfigNode(name = "WebP.compressionType")
    public String getWebPCompressionType() {
        return webp.compressionType;
    }

    @ConfigNode(name = "imagePath")
    public String getImagePath() {
        return imagePath;
    }

     static class PNG {
        private final boolean enabled;
        private final double compressionQuality;
        
        private PNG() {
            enabled = false;
            compressionQuality = 75;
        }
    }

    static class JPEG {
        private final boolean enabled;
        private final double compressionQuality;

        private JPEG() {
            enabled = true;
            compressionQuality = 75;
        }
    }

    static class GIF {
        private final boolean enabled;
        private final double compressionQuality;

        private GIF() {
            enabled = false;
            compressionQuality = 75;
        }
    }

    static class BMP {
        private final boolean enabled;
        private final double compressionQuality;
        private final String compressionType;

        private BMP() {
            enabled = false;
            compressionQuality = 75;
            //BI_RGB, BI_RLE8, BI_RLE4, BI_BITFIELDS, BI_JPEG, BI_PNG
            compressionType = "BI_RGB";
        }
    }

    static class WebP {
        private final boolean enabled;
        private final String compressionType;

        private WebP() {
            enabled = true;
            compressionType = "DEFAULT";
        }
    }
}
