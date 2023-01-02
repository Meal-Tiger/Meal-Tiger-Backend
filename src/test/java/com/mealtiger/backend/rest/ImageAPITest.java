package com.mealtiger.backend.rest;

import com.mealtiger.backend.BackendApplication;
import com.mealtiger.backend.configuration.Configurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = {BackendApplication.class}
)
class ImageAPITest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private Configurator configurator;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() throws IOException {
        if(Files.exists(Path.of(configurator.getString("Image.imagePath")))) {
            deleteFile(Path.of(configurator.getString("Image.imagePath")));
        }
    }

    @ParameterizedTest
    @MethodSource("fileStream")
    void postImageTest(File inputFile) throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        mvc.perform(multipart("/image")
                .file(file)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\\\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\\\"")));
    }

    @Test
    void postImagesTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        File inputFile = fileStream().toList().get(0);
        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        MvcResult result = mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\\\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\\\"")))
                .andReturn();

        String uuid = result.getResponse().getContentAsString().substring(1,37);

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"))
                .andExpect(status().isOk());
    }

    @Test
    void getImageAcceptHeaderTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        File inputFile = fileStream().toList().get(0);
        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        MvcResult result = mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\\\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\\\"")))
                .andReturn();

        String uuid = result.getResponse().getContentAsString().substring(1,37);

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/webp"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/webp"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/gif"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/gif"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/bmp"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/bmp"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/jpeg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/jpeg,*/*;q=0.8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/jpeg;q=0.9,image/png;q=0.8;*/*;q=0.7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/webp;q=0.9,image/jpeg;q=0.8;*/*;q=0.7"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/webp"));
    }

    @Test
    void deleteImageTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        File inputFile = fileStream().toList().get(0);
        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        MvcResult result = mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matchesPattern("\\\"[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}\\\"")))
                .andReturn();

        String uuid = result.getResponse().getContentAsString().substring(1,37);

        mvc.perform(delete("/image/" + uuid))
                .andExpect(status().isOk());
    }

    // NEGATIVE TESTS

    @Test
    void postImageUnsupportedType() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        File inputFile = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("com/mealtiger/backend/imageio/testImages/DefaultTestImage/TestImage.pdf")).getFile());

        MockMultipartFile file;

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            byte[] input = inputStream.readAllBytes();
            file = new MockMultipartFile("file", input);
        }

        mvc.perform(multipart("/image")
                        .file(file)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void postImagesUnsupportedType() throws Exception {
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void getImageNotFoundTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/bmp;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getImageNotAcceptableTest() throws Exception {
        when(configurator.getString("Image.servedImageMediaTypes")).thenReturn("image/png;q=1.0,image/jpeg;q=1.0,image/webp;q=1.0,image/gif;q=1.0");
        when(configurator.getString("Image.servedImageFormats")).thenReturn("png,jpeg,gif,webp,bmp");

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(get("/image/" + uuid)
                        .header("Accept", "image/bmp"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void deleteImageNotFoundTest() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String uuid = UUID.randomUUID().toString();

        mvc.perform(delete("/image/" + uuid))
                .andExpect(status().isNotFound());
    }

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
