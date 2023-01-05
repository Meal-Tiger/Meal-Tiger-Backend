package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.rest.controller.ImageIOController;
import com.mealtiger.backend.rest.exceptions.UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This rest controller is supposed to process image-related requests.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@RestController
@Configuration
public class ImageAPI {

    private static final Logger log = LoggerFactory.getLogger(ImageAPI.class);
    private final ImageIOController controller;
    private final Configurator configurator;

    public ImageAPI(ImageIOController controller, Configurator configurator) {
        this.controller = controller;
        this.configurator = configurator;
    }

    @PostMapping(value = "/images")
    public ResponseEntity<List<UUID>> postMultipleImages(@RequestParam("files") MultipartFile[] files) throws UploadException {
        log.debug("Uploading multiple images!");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<UUID> uuids = new ArrayList<>();

        for(MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    // Image format is not supported!
                    for (UUID alreadySavedUUID : uuids) {
                        deleteImage(alreadySavedUUID.toString());
                    }
                    return ResponseEntity.badRequest().build();
                }
                controller.saveImage(image, String.valueOf(uuid), userId);
            } catch (IOException e) {
                throw new UploadException("Could not open uploaded file " + file.getName());
            } finally {
                uuids.add(uuid);
            }
        }

        return ResponseEntity.ok(uuids);
    }

    @PostMapping(value = "/image")
    public ResponseEntity<UUID> postImage(@RequestParam("file") MultipartFile file) throws UploadException {
        log.debug("Uploading a single image!");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        UUID uuid = UUID.randomUUID();

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                // Image format is not supported!
                return ResponseEntity.badRequest().build();
            }
            controller.saveImage(image, String.valueOf(uuid), userId);
        } catch (IOException e) {
            throw new UploadException("Could not open uploaded file " + file.getName() + ". Reason: " + e.getMessage());
        }

        return ResponseEntity.ok(uuid);
    }

    @GetMapping(value = "/image/{uuid}")
    public ResponseEntity<Resource> getImage(@PathVariable(value = "uuid") String uuid, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        List<MediaType> acceptedMediaTypeList = MediaType.parseMediaTypes(acceptHeader);
        return controller.getBestSuitedImage(uuid, acceptedMediaTypeList);
    }

    @DeleteMapping("/image/{uuid}")
    public ResponseEntity<Void> deleteImage(@PathVariable(value = "uuid") String uuid) {
        if (!controller.doesImageExist(uuid)) {
            return ResponseEntity.notFound().build();
        }

        String adminRole = configurator.getString("Authentication.OIDC.adminRole");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(adminRole));

        log.debug("Deleting image with uuid {}!", uuid);

        return controller.deleteImage(uuid, userId, isAdmin);
    }
}
