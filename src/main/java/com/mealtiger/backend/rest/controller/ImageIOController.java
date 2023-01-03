package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.imageio.adapters.ImageAdapter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This class provides a facade to the ImageIO implementation.
 *
 * @author Lucca Greschner
 */
@Service
public class ImageIOController {

    private final Configurator configurator;

    private final ImageAdapter bitmapAdapter;
    private final ImageAdapter gifAdapter;
    private final ImageAdapter jpegAdapter;
    private final ImageAdapter pngAdapter;
    private final ImageAdapter webPAdapter;

    public ImageIOController(ImageAdapter bitmapAdapter, ImageAdapter gifAdapter, ImageAdapter jpegAdapter, ImageAdapter pngAdapter, ImageAdapter webPAdapter, Configurator configurator) {
        this.bitmapAdapter = bitmapAdapter;
        this.gifAdapter = gifAdapter;
        this.jpegAdapter = jpegAdapter;
        this.pngAdapter = pngAdapter;
        this.webPAdapter = webPAdapter;

        this.configurator = configurator;
    }

    public void saveImage(BufferedImage image, String uuid) throws IOException {
        String servedFormats = configurator.getString("Image.servedImageFormats");
        List<String> servedFormatsSplitted = List.of(servedFormats.split(","));

        String path = configurator.getString("Image.imagePath");

        File filePath = new File(path + uuid);

        File parent = filePath.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create directory: " + parent);

        }

        if (!filePath.exists() && !filePath.mkdir()) {
            throw new IllegalStateException("Couldn't create directory: " + filePath);
        }

        for (String format : servedFormatsSplitted) {
            byte[] imageBytes;

            switch (format) {
                case "bmp" -> imageBytes = bitmapAdapter.convert(image);
                case "jpeg", "jpg" -> imageBytes = jpegAdapter.convert(image);
                case "gif" -> imageBytes = gifAdapter.convert(image);
                case "png" -> imageBytes = pngAdapter.convert(image);
                case "webp" -> imageBytes = webPAdapter.convert(image);
                default -> throw new IllegalArgumentException("Image format unknown: " + format);
            }

            File file = new File(path + uuid + "/image." + format);
            if (!file.createNewFile()) {
                throw new IllegalStateException("Couldn't create file: " + file);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(imageBytes);
            }
        }
    }
}
