package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.imageio.ImageIOFacade;
import com.mealtiger.backend.rest.exceptions.UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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

    private static final MediaType IMAGE_JPEG = MediaType.IMAGE_JPEG;
    private static final MediaType IMAGE_PNG = MediaType.IMAGE_PNG;
    private static final MediaType IMAGE_WEBP = new MediaType("image", "webp");
    private static final MediaType IMAGE_GIF = new MediaType("image", "gif");
    private static final MediaType IMAGE_BMP = new MediaType("image", "bmp");

    private final Configurator configurator;
    private final Path imageRootPath;

    public ImageAPI(Configurator configurator) {
        this.configurator = configurator;
        this.imageRootPath = Path.of(configurator.getString("Image.imagePath"));
    }

    @PostMapping(value = "/images")
    public ResponseEntity<List<UUID>> postMultipleImages(@RequestParam("files") MultipartFile[] files) throws UploadException {
        log.debug("Uploading multiple images!");

        List<UUID> uuids = new ArrayList<>();

        for(MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            try (InputStream inputStream = file.getInputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                ImageIOFacade imageIOFacade = new ImageIOFacade();
                imageIOFacade.saveImage(image, String.valueOf(uuid));
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

        UUID uuid = UUID.randomUUID();

        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIOFacade imageIOFacade = new ImageIOFacade();
            imageIOFacade.saveImage(image, String.valueOf(uuid));
        } catch (IOException e) {
            throw new UploadException("Could not open uploaded file " + file.getName() + ". Reason: " + e.getMessage());
        }

        return ResponseEntity.ok(uuid);
    }

    @GetMapping(value = "/image/{uuid}")
    public ResponseEntity<Resource> getImage(@PathVariable(value = "uuid") String uuid, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
        List<MediaType> acceptedMediaTypeList = MediaType.parseMediaTypes(acceptHeader);
        List<MediaType> servedMediaTypeList = MediaType.parseMediaTypes(configurator.getString("Image.servedImageMediaTypes"));

        MediaType bestSuitedMediaType = findBestMatch(acceptedMediaTypeList, servedMediaTypeList);

        if (bestSuitedMediaType == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        if (bestSuitedMediaType.isCompatibleWith(IMAGE_WEBP)) {
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.webp"), IMAGE_WEBP);
        }

        if (bestSuitedMediaType.isCompatibleWith(IMAGE_PNG)) {
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.png"), IMAGE_PNG);
        }

        if (bestSuitedMediaType.isCompatibleWith(IMAGE_JPEG)) {
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.jpg"), IMAGE_JPEG);
        }

        if (bestSuitedMediaType.isCompatibleWith(IMAGE_GIF)) {
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.gif"), IMAGE_GIF);
        }

        if (bestSuitedMediaType.isCompatibleWith(IMAGE_BMP)) {
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.bmp"), IMAGE_BMP);
        }

        // Never reached
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("/image/{uuid}")
    public ResponseEntity<Void> deleteImage(@PathVariable(value = "uuid") String uuid) throws IOException {
        log.debug("Deleting image with uuid {}!", uuid);

        Path path = imageRootPath.resolve(uuid);
        deleteFile(path);

        return ResponseEntity.ok(null);
    }

    // HELPER METHODS

    /**
     * Retrieves an image from disk by its media type
     * @param path Path to the image directory.
     * @param type Type of the image to retrieve.
     * @return ResponseEntity ready to be served.
     */
    private ResponseEntity<Resource> getImageFromDisk(Path path, MediaType type) {
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            log.trace("Successfully retrieved file {}!", path);
            return ResponseEntity.ok()
                    .contentLength(path.toFile().length())
                    .contentType(type)
                    .body(resource);
        } catch (FileNotFoundException e) {
            log.debug("File {} not found!", path);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            log.error("Error upon downloading file {}: {}", imageRootPath, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Finds the best match between those media types the client accepts and those the server serves.
     * @param acceptedTypes List of types the client accepts
     * @param servedTypes List of types the server serves
     * @return MediaType to be served.
     */
    private MediaType findBestMatch(List<MediaType> acceptedTypes, List<MediaType> servedTypes) {
        MediaType bestSuitedMediaType = null;
        double clientSatisfaction = 0;
        double serverSatisfaction = 0;

        for (MediaType acceptedType : acceptedTypes) {
            for (MediaType servedType : servedTypes) {
                if (acceptedType.isCompatibleWith(servedType) && (bestSuitedMediaType == null
                        || clientSatisfaction < acceptedType.getQualityValue()
                        || serverSatisfaction < servedType.getQualityValue())) {
                    bestSuitedMediaType = new MediaType(servedType.getType(),
                            servedType.getSubtype());
                    clientSatisfaction = acceptedType.getQualityValue();
                    serverSatisfaction = servedType.getQualityValue();
                }
            }
        }


        log.trace("Found best match of media types {}", bestSuitedMediaType);

        return bestSuitedMediaType;
    }

    /**
     * Deletes a file. If the given path is a directory it is deleted recursively.
     * @param path Path to be deleted.
     * @throws IOException Thrown whenever a file cannot be deleted.
     */
    private void deleteFile(Path path) throws IOException {
        Files.walkFileTree(path,
            new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult postVisitDirectory(
                        Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(
                        Path file, BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            }
        );
    }
}
