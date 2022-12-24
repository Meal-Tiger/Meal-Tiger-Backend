package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.imageio.ImageIOFacade;
import com.mealtiger.backend.rest.exceptions.UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * This rest controller is supposed to process image-related requests.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@RestController
public class ImageAPI {

    private static final Logger log = LoggerFactory.getLogger(ImageAPI.class);

    @PostMapping("/image")
    public ResponseEntity<UUID> postImage(@RequestBody MultipartFile[] files) throws UploadException {
        log.debug("Uploading images!");

        UUID uuid = UUID.randomUUID();

        for(MultipartFile file : files) {
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                ImageIOFacade imageIOFacade = new ImageIOFacade();
                imageIOFacade.saveImage(image, String.valueOf(uuid));
            } catch (IOException e) {
                throw new UploadException("Could not open uploaded file " + file.getName());
            }
        }

        return ResponseEntity.ok(uuid);
    }

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<MultipartFile> getImage(@PathVariable(value = "id") int id) {
        log.trace("Getting png image with id {}!", id);

        Configurator configurator = new Configurator();
        String path = configurator.getString("Image.imagePath");

        // DUMMY!

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable(value = "id") int id) {
        log.debug("Deleting image with id {}!", id);
        // DUMMY!
        return ResponseEntity.ok(null);
    }
}
