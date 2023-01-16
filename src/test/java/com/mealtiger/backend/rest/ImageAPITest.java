package com.mealtiger.backend.rest;

import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.configuration.Configurator;
import com.mealtiger.backend.rest.controller.ImageIOController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.mealtiger.backend.SampleSource.SAMPLE_IMAGE_ID;
import static com.mealtiger.backend.SampleSource.SAMPLE_OTHER_USER_ID;
import static com.mealtiger.backend.SampleSource.SAMPLE_USER_ID;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ImageAPI.
 *
 * @author Sebastian Maier, Lucca Greschner
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
@AutoConfigureMockMvc
@Tag("integration")
class ImageAPITest {
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ImageIOController imageIOController;

    @Autowired
    private MockMvc mvc;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private Configurator configurator;

    /**
     * Deletes all images created in tests.
     */
    @BeforeEach
    @AfterEach
    void beforeAfterEach() throws IOException {
        if(Files.exists(Path.of(configurator.getString("Image.imagePath")))) {
            Helper.deleteFile(Path.of(configurator.getString("Image.imagePath")));
        }
    }

    /**
     * Tests posting single images.
     * @param inputFile image file provided by method source.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @ParameterizedTest
    @MethodSource("fileStream")
    void postImageTest(File inputFile) throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        mvc.perform(multipart("/image")
                .file(file)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\"")));
    }

    /**
     * Tests posting multiple images.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void postImagesTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMultipartFile file1;
        MockMultipartFile file2;

        File inputFile1 = fileStream().toList().get(0);
        File inputFile2 = fileStream().toList().get(1);

        try (InputStream inputStream1 = new FileInputStream(inputFile1);
            InputStream inputStream2 = new FileInputStream(inputFile2)) {
            byte[] input1 = inputStream1.readAllBytes();
            file1 = new MockMultipartFile("files", input1);

            byte[] input2 = inputStream2.readAllBytes();
            file2 = new MockMultipartFile("files", input2);
        }

        mvc.perform(multipart("/images")
                        .file(file1)
                        .file(file2)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /**
     * Tests getting images.
     */
    @Test
    void getImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        File inputFile = fileStream().toList().get(0);
        saveImage(inputFile, SAMPLE_IMAGE_ID, SAMPLE_USER_ID);

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"))
                .andExpect(status().isOk());
    }

    /**
     * Tests getting images using different accept headers.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void getImageAcceptHeaderTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        File inputFile = fileStream().toList().get(0);
        saveImage(inputFile, SAMPLE_IMAGE_ID, SAMPLE_USER_ID);

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/webp"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/webp"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/gif"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/gif"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/bmp"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/bmp"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/jpeg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/jpeg,*/*;q=0.8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/jpeg;q=0.9,image/png;q=0.8;*/*;q=0.7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + SAMPLE_IMAGE_ID)
                        .header("Accept", "image/webp;q=0.9,image/jpeg;q=0.8;*/*;q=0.7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/webp"));
    }

    /**
     * Tests deleting images.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void deleteImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        File inputFile = fileStream().toList().get(0);
        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        MvcResult result = mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\"")))
                .andReturn();

        String uuid = result.getResponse().getContentAsString().substring(1,37);

        mvc.perform(delete("/image/" + uuid))
                .andExpect(status().isNoContent());
    }

    // NEGATIVE TESTS

    /**
     * Tests posting a single file of unsupported type. In this case a PDF-Document.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_400_postImageUnsupportedTypeTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        File inputFile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.pdf")).getFile());

        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/image"));
    }

    /**
     * Tests posting on "/image" while unauthorized.
     */
    @Test
    void negative_401_postImageTest() throws Exception {
        mvc.perform(multipart("/image"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests posting multiple files with one of unsupported type. In this case a PDF-Document.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_400_postImagesUnsupportedTypeTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartFile file1;
        MockMultipartFile file2;

        File inputFile1 = fileStream().toList().get(0);
        File inputFile2 = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.pdf")).getFile());

        try (InputStream inputStream1 = new FileInputStream(inputFile1);
             InputStream inputStream2 = new FileInputStream(inputFile2)) {
            byte[] input1 = inputStream1.readAllBytes();
            file1 = new MockMultipartFile("files", input1);

            byte[] input2 = inputStream2.readAllBytes();
            file2 = new MockMultipartFile("files", input2);
        }

        mvc.perform(multipart("/images")
                        .file(file1)
                        .file(file2)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/images"));
    }

    /**
     * Tests posting on "/images" while unauthorized.
     */
    @Test
    void negative_401_postImagesTest() throws Exception {
        mvc.perform(multipart("/images"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests whether not found (404) is returned when an image does not exist.
     */
    @Test
    void negative_404_getImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/image/" + uuid));
    }

    /**
     * Tests whether not acceptable (406) is returned when an image type is not served.
     */
    @Test
    void negative_406_getImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/bmp"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/image/" + uuid));
    }

    /**
     * Tests deleting on "/images/{imageId}" while unauthorized.
     */
    @Test
    void negative_401_deleteImageTest() throws Exception {
        mvc.perform(delete("/image/" + SAMPLE_IMAGE_ID))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests deleting a foreign image.
     */
    @WithMockUser("123e4567-e89b-12d3-a456-42661417400")
    @Test
    void negative_403_deleteImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        File inputFile = fileStream().toList().get(0);

        saveImage(inputFile, SAMPLE_IMAGE_ID, SAMPLE_OTHER_USER_ID);

        mvc.perform(delete("/image/" + SAMPLE_IMAGE_ID))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests deleting an image that doesn't exist.
     */
    @Test
    void negative_404_deleteImageTest() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(delete("/image/" + uuid))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.path").value("/image/" + uuid));
    }

    /**
     * Provides a stream of image files.
     * @return Stream containing the image test files.
     */
    static Stream<File> fileStream() {
        return Stream.of(
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.bmp")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.gif")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.jpg")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.lossless.webp")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.png")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.tiff")).getFile()),
                new File(Objects.requireNonNull(ImageAPITest.class.getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.webp")).getFile())
        );
    }

    // HELPER METHODS

    /**
     * Saves an image.
     * @param file Image file to be saved.
     * @param uuid UUID of the image.
     * @param userId UserID of the creating user.
     */
    private void saveImage(File file, String uuid, String userId) throws IOException {
        BufferedImage image = ImageIO.read(file);
        imageIOController.saveImage(image, uuid, userId);
    }

}
