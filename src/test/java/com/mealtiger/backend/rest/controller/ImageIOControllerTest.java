package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import com.mealtiger.backend.database.repository.ImageMetadataRepository;
import com.mealtiger.backend.imageio.adapters.*;
import com.mealtiger.backend.rest.Helper;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static com.mealtiger.backend.SampleSource.SAMPLE_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
@Tag("unit")
class ImageIOControllerTest {

    @Mock
    private Configurator configurator;
    @Mock
    private BitmapAdapter bitmapAdapter;
    @Mock
    private GIFAdapter gifAdapter;
    @Mock
    private JPEGAdapter jpegAdapter;
    @Mock
    private PNGAdapter pngAdapter;
    @Mock
    private WebPAdapter webPAdapter;
    @Mock
    private ImageMetadataRepository imageMetadataRepository;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() throws IOException {
        Path imagePath = Path.of("testImages/");

        if(Files.exists(imagePath)) {
            Helper.deleteFile(imagePath);
        }
    }

    /**
     * Tests reading images.
     */
    @Test
    void readImageTest() throws IOException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        MockMultipartFile multipartFile = spy(new MockMultipartFile("file", this.getClass().getClassLoader().getResourceAsStream("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.jpg")));
        BufferedImage image = controller.readImage(multipartFile);

        verify(multipartFile).getInputStream();
        assertNotEquals(0, image.getHeight());
        assertNotEquals(0, image.getWidth());
    }

    /**
     * Tests saving images.
     */
    @Test
    void saveImageTest() throws IOException {
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        BufferedImage image = new BufferedImage(256,256,BufferedImage.TYPE_INT_RGB);

        when(bitmapAdapter.convert(any())).thenReturn(new byte[]{});
        when(jpegAdapter.convert(any())).thenReturn(new byte[]{});
        when(gifAdapter.convert(any())).thenReturn(new byte[]{});
        when(pngAdapter.convert(any())).thenReturn(new byte[]{});
        when(webPAdapter.convert(any())).thenReturn(new byte[]{});

        controller.saveImage(image, SAMPLE_IMAGE_ID, SAMPLE_USER_ID);

        verify(bitmapAdapter).convert(any());
        verify(jpegAdapter).convert(any());
        verify(gifAdapter).convert(any());
        verify(pngAdapter).convert(any());
        verify(webPAdapter).convert(any());

        verify(imageMetadataRepository).save(new ImageMetadata(SAMPLE_IMAGE_ID, SAMPLE_USER_ID));
    }

    /**
     * Tests getting the best suited image mediatypes with only one given accepted mediatype.
     */
    @Test
    void getBestSuitedImageTest() throws IOException, HttpMediaTypeNotAcceptableException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");

        createTestImages();

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);
        assertEquals("BMP", getResourceAsString(controller, List.of(MediaType.valueOf("image/bmp"))));
        assertEquals("JPEG", getResourceAsString(controller, List.of(MediaType.IMAGE_JPEG)));
        assertEquals("GIF", getResourceAsString(controller, List.of(MediaType.valueOf("image/gif"))));
        assertEquals("PNG", getResourceAsString(controller, List.of(MediaType.IMAGE_PNG)));
        assertEquals("WEBP", getResourceAsString(controller, List.of(MediaType.valueOf("image/webp"))));

        assertThrowsExactly(HttpMediaTypeNotAcceptableException.class, () -> controller.getBestSuitedImage(SAMPLE_IMAGE_ID, List.of(MediaType.TEXT_HTML)));
        assertThrowsExactly(HttpMediaTypeNotAcceptableException.class, () -> controller.getBestSuitedImage(SAMPLE_IMAGE_ID, List.of(MediaType.valueOf("image/tiff"))));
    }

    /**
     * Tests getting the best suited image mediatype with the Chrome/Safari accept header.
     */
    @Test
    void chromeSafariAcceptHeaderTest() throws IOException, HttpMediaTypeNotAcceptableException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");

        createTestImages();

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        // CHROME / SAFARI ACCEPT HEADER
        // ALL MEDIATYPES SERVED
        assertEquals("WEBP", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")));
        // ALL BUT WEBP SERVED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/gif;q=1.0");
        assertEquals("PNG", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")));
        // ALL BUT WEBP SERVED, BUT JPEG IS PREFERRED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=0.9,image/jpeg;q=1.0,image/bmp;q=0.9,image/gif;q=0.9");
        assertEquals("JPEG", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")));
    }

    /**
     * Tests getting the best suited image mediatype with the Internet Explorer accept header.
     */
    @Test
    void internetExplorerAcceptHeaderTest() throws IOException, HttpMediaTypeNotAcceptableException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");

        createTestImages();

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        // INTERNET EXPLORER ACCEPT HEADER
        // ALL MEDIATYPES SERVED
        assertEquals("JPEG", getResourceAsString(controller, MediaType.parseMediaTypes("image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/msword, */*")));
        // ALL BUT JPEG SERVED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/bmp;q=1.0,image/gif;q=1.0");
        assertEquals("GIF", getResourceAsString(controller, MediaType.parseMediaTypes("image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/msword, */*")));
        // ALL BUT WEBP SERVED, BUT PNG IS PREFERRED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/bmp;q=0.9,image/gif;q=0.9");
        assertEquals("PNG", getResourceAsString(controller, MediaType.parseMediaTypes("image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/msword, */*")));
    }

    /**
     * Tests getting the best suited image mediatype with the Firefox accept header.
     */
    @Test
    void firefoxAcceptHeaderTest() throws IOException, HttpMediaTypeNotAcceptableException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");

        createTestImages();

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        // FIREFOX ACCEPT HEADER
        // ALL MEDIATYPES SERVED
        assertEquals("WEBP", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")));
        // ALL BUT WEBP SERVED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/gif;q=1.0");
        assertEquals("PNG", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")));
        // ALL BUT WEBP SERVED, BUT JPEG IS PREFERRED
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=0.9,image/jpeg;q=1.0,image/bmp;q=0.9,image/gif;q=0.9");
        assertEquals("JPEG", getResourceAsString(controller, MediaType.parseMediaTypes("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")));
    }

    /**
     * Tests deleting an image.
     */
    @Test
    void deleteImageTest() throws IOException {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");

        createTestImages();

        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        when(imageMetadataRepository.findById(SAMPLE_IMAGE_ID)).thenReturn(Optional.of(new ImageMetadata(SAMPLE_IMAGE_ID, SAMPLE_USER_ID)));
        assertEquals(ResponseEntity.noContent().build(), controller.deleteImage(SAMPLE_IMAGE_ID, SAMPLE_USER_ID, false));

        // UNAUTHORIZED

        createTestImages();

        when(imageMetadataRepository.findById(SAMPLE_IMAGE_ID)).thenReturn(Optional.of(new ImageMetadata(SAMPLE_IMAGE_ID, "6809c76b-5a48-44fe-85bf-d44cef12a828")));
        assertEquals(ResponseEntity.status(HttpStatus.FORBIDDEN).build(), controller.deleteImage(SAMPLE_IMAGE_ID, SAMPLE_USER_ID, false));

        // UNAUTHORIZED BUT ADMIN
        when(imageMetadataRepository.findById(SAMPLE_IMAGE_ID)).thenReturn(Optional.of(new ImageMetadata(SAMPLE_IMAGE_ID, "6809c76b-5a48-44fe-85bf-d44cef12a828")));
        assertEquals(ResponseEntity.noContent().build(), controller.deleteImage(SAMPLE_IMAGE_ID, SAMPLE_USER_ID, true));

        // NOT FOUND

        when(imageMetadataRepository.findById(SAMPLE_IMAGE_ID)).thenReturn(Optional.of(new ImageMetadata("6809c76b-5a48-44fe-85bf-d44cef12a828", SAMPLE_USER_ID)));
        assertThrowsExactly(EntityNotFoundException.class, () -> controller.deleteImage(SAMPLE_IMAGE_ID, SAMPLE_USER_ID, false));
    }

    /**
     * Tests the doesImageExist method in ImageIOController.
     */
    @Test
    void doesImageExistTest() {
        when(configurator.getString("Image.imagePath")).thenReturn("testImages/");
        ImageIOController controller = new ImageIOController(bitmapAdapter, gifAdapter, jpegAdapter, pngAdapter, webPAdapter, configurator, imageMetadataRepository);

        when(imageMetadataRepository.existsById(SAMPLE_IMAGE_ID)).thenReturn(true);
        assertTrue(controller.doesImageExist(SAMPLE_IMAGE_ID));

        when(imageMetadataRepository.existsById(SAMPLE_IMAGE_ID)).thenReturn(false);
        assertFalse(controller.doesImageExist(SAMPLE_IMAGE_ID));
    }

    // HELPER METHODS

    /**
     * This method creates dummy image files for the different formats.
     */
    private void createTestImages() throws IOException {
        Files.createDirectories(Path.of("testImages/" + SAMPLE_IMAGE_ID));

        File bmpFile = new File(Path.of("testImages/" + SAMPLE_IMAGE_ID + "/image.bmp").toUri());
        assertTrue(bmpFile.createNewFile());

        try (FileWriter writer = new FileWriter(bmpFile)) {
            writer.write("BMP");
        }

        File jpegFile = new File(Path.of("testImages/" + SAMPLE_IMAGE_ID + "/image.jpeg").toUri());
        assertTrue(jpegFile.createNewFile());

        try (FileWriter writer = new FileWriter(jpegFile)) {
            writer.write("JPEG");
        }

        File pngFile = new File(Path.of("testImages/" + SAMPLE_IMAGE_ID + "/image.png").toUri());
        assertTrue(pngFile.createNewFile());

        try (FileWriter writer = new FileWriter(pngFile)) {
            writer.write("PNG");
        }

        File gifFile = new File(Path.of("testImages/" + SAMPLE_IMAGE_ID + "/image.gif").toUri());
        assertTrue(gifFile.createNewFile());

        try (FileWriter writer = new FileWriter(gifFile)) {
            writer.write("GIF");
        }

        File webPFile = new File(Path.of("testImages/" + SAMPLE_IMAGE_ID + "/image.webp").toUri());
        assertTrue(webPFile.createNewFile());

        try (FileWriter writer = new FileWriter(webPFile)) {
            writer.write("WEBP");
        }
    }

    /**
     * Gets the result of the getBestSuitedImage method in ImageIOController and converts its body to a String.
     * @param controller ImageIOController to be used to call the method on.
     * @param acceptedMediaTypes MediaTypes accepted.
     * @return Content as a string (Attention: Only up to 16 Bytes/characters!).
     */
    private String getResourceAsString(ImageIOController controller, List<MediaType> acceptedMediaTypes) throws IOException, HttpMediaTypeNotAcceptableException {
        String result;

        try (ReadableByteChannel channel = Objects.requireNonNull(controller.getBestSuitedImage(SAMPLE_IMAGE_ID, acceptedMediaTypes).getBody()).readableChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            channel.read(buffer);
            result = new String(buffer.array());
        }

        return result.trim();
    }

}
