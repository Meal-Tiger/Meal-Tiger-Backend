package com.mealtiger.backend.imageio;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.imageio.adapters.ImageAdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides a facade to the ImageIO implementation.
 *
 * @author Lucca Greschner
 */
public class ImageIOFacade {
    private static final Logger log = LoggerFactory.getLogger(ImageIOFacade.class);
    public void saveImage(BufferedImage image, String uuid) throws IOException {
        Configurator configurator = new Configurator();
        String servedFormats = configurator.getString("Image.servedImageFormats");
        List<String> servedFormatsSplitted = List.of(servedFormats.split(","));

        String path = configurator.getString("Image.imagePath");

        File filePath = new File(path + uuid);

        File parent = filePath.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create directory: " + parent);

        }

        if (!filePath.exists()) {
            if (!filePath.mkdir()) {
                throw new IllegalStateException("Couldn't create directory: " + filePath);
            }
        }

        for (String format : servedFormatsSplitted) {
            ImageAdapterFactory factory = new ImageAdapterFactory(format, image);
            byte[] imageBytes = factory.getInstance().convert();

            log.info("Got the following byte array for format {}: {}", format, Arrays.toString(imageBytes));

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
