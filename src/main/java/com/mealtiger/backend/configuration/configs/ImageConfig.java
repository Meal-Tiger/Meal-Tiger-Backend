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
@Config(name = "Image", configPath = "image.yml", sampleConfig = "configuration-samples/image.sample.yml")
public class ImageConfig {

    private final String imagePath;
    private final String maxFileSize;
    
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
        maxFileSize = "5MB";
    }

    @ConfigNode(name = "maxFileSize", envKey = "MAX_IMAGE_FILE_SIZE",
            springProperties = {"spring.servlet.multipart.max-file-size", "spring.servlet.multipart.max-request-size"})
    public String getMaxFileSize() {
        return maxFileSize;
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
            stringBuilder.append("webp");
        }
        return stringBuilder.toString();
    }

    @ConfigNode(name = "servedImageMediaTypes")
    public String getServedImageMediaTypes() {
        StringBuilder stringBuilder = new StringBuilder();
        if (png.enabled) {
            stringBuilder.append("image/png;q=").append(png.qualityWeighting).append(",");
        }
        if (jpeg.enabled) {
            stringBuilder.append("image/jpeg;q=").append(jpeg.qualityWeighting).append(",");
        }
        if (gif.enabled) {
            stringBuilder.append("image/gif;q=").append(gif.qualityWeighting).append(",");
        }
        if (bmp.enabled) {
            stringBuilder.append("image/bmp;q=").append(bmp.qualityWeighting).append(",");
        }
        if (webp.enabled) {
            stringBuilder.append("image/webp;q=").append(webp.qualityWeighting);
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

    @ConfigNode(name = "imagePath", envKey = "IMAGE_PATH")
    public String getImagePath() {
        return imagePath;
    }

     static class PNG {
        private final boolean enabled;
        private final double compressionQuality;
        private final double qualityWeighting;

        private PNG() {
            enabled = false;
            compressionQuality = 75;
            qualityWeighting = 0.8;
        }
    }

    static class JPEG {
        private final boolean enabled;
        private final double compressionQuality;
        private final double qualityWeighting;

        private JPEG() {
            enabled = true;
            compressionQuality = 75;
            qualityWeighting = 0.9;
        }
    }

    static class GIF {
        private final boolean enabled;
        private final double compressionQuality;
        private final double qualityWeighting;

        private GIF() {
            enabled = false;
            compressionQuality = 75;
            qualityWeighting = 0.7;
        }
    }

    static class BMP {
        private final boolean enabled;
        private final double compressionQuality;
        private final String compressionType;
        private final double qualityWeighting;

        private BMP() {
            enabled = false;
            compressionQuality = 75;
            //BI_RGB, BI_RLE8, BI_RLE4, BI_BITFIELDS, BI_JPEG, BI_PNG
            compressionType = "BI_RGB";
            qualityWeighting = 0.5;
        }
    }

    static class WebP {
        private final boolean enabled;
        private final String compressionType;
        private final double qualityWeighting;

        private WebP() {
            enabled = true;
            compressionType = "DEFAULT";
            qualityWeighting = 1.0;
        }
    }
}
