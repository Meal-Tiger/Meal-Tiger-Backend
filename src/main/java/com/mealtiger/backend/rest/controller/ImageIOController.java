package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import com.mealtiger.backend.database.repository.ImageMetadataRepository;
import com.mealtiger.backend.imageio.adapters.ImageAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * This class provides a facade to the ImageIO implementation.
 *
 * @author Lucca Greschner
 */
@Service
public class ImageIOController {

    private static final Logger log = LoggerFactory.getLogger(ImageIOController.class);

    private static final MediaType IMAGE_JPEG = MediaType.IMAGE_JPEG;
    private static final MediaType IMAGE_PNG = MediaType.IMAGE_PNG;
    private static final MediaType IMAGE_WEBP = new MediaType("image", "webp");
    private static final MediaType IMAGE_GIF = new MediaType("image", "gif");
    private static final MediaType IMAGE_BMP = new MediaType("image", "bmp");

    private final Configurator configurator;

    private final ImageMetadataRepository imageMetadataRepository;

    private final ImageAdapter bitmapAdapter;
    private final ImageAdapter gifAdapter;
    private final ImageAdapter jpegAdapter;
    private final ImageAdapter pngAdapter;
    private final ImageAdapter webPAdapter;

    private final Path imageRootPath;

    public ImageIOController(ImageAdapter bitmapAdapter,
                             ImageAdapter gifAdapter,
                             ImageAdapter jpegAdapter,
                             ImageAdapter pngAdapter,
                             ImageAdapter webPAdapter,
                             Configurator configurator,
                             ImageMetadataRepository imageMetadataRepository) {
        this.bitmapAdapter = bitmapAdapter;
        this.gifAdapter = gifAdapter;
        this.jpegAdapter = jpegAdapter;
        this.pngAdapter = pngAdapter;
        this.webPAdapter = webPAdapter;

        this.configurator = configurator;
        this.imageMetadataRepository = imageMetadataRepository;
        this.imageRootPath = Path.of(configurator.getString("Image.imagePath"));
    }

    public void saveImage(BufferedImage image, String uuid, String userId) throws IOException {
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

        imageMetadataRepository.save(new ImageMetadata(uuid, userId));
    }

    public ResponseEntity<Resource> getBestSuitedImage(String uuid, List<MediaType> acceptedMediaTypes) {
        List<MediaType> servedMediaTypeList = MediaType.parseMediaTypes(configurator.getString("Image.servedImageMediaTypes"));

        MediaType bestSuitedMediaType = findBestMatch(acceptedMediaTypes, servedMediaTypeList);

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
            return getImageFromDisk(imageRootPath.resolve(uuid).resolve("image.jpeg"), IMAGE_JPEG);
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

    public ResponseEntity<Void> deleteImage(String uuid, String userId) {

        ImageMetadata imageMetadata = imageMetadataRepository.findById(uuid).orElse(null);

        if (imageMetadata == null) {
            return ResponseEntity.internalServerError().build();
        }

        if (!imageMetadata.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Path path = imageRootPath.resolve(uuid);
        try {
            deleteFile(path);
        } catch (FileNotFoundException | NoSuchFileException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        imageMetadataRepository.deleteById(uuid);

        return ResponseEntity.ok(null);
    }

    /**
     * @param uuid UUID of the image to be looked up.
     * @return True if the image exists in the database, false if not.
     */
    public boolean doesImageExist(String uuid) {
        return imageMetadataRepository.existsById(uuid);
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
        } catch (FileNotFoundException | NoSuchFileException e) {
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
