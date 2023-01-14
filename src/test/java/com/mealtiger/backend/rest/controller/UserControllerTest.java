package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.SampleSource;
import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.user.UserMetadata;
import com.mealtiger.backend.database.repository.ImageMetadataRepository;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.database.repository.UserMetadataRepository;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.model.recipe.RecipeResponse;
import com.mealtiger.backend.rest.model.user.UserInformationRequest;
import com.mealtiger.backend.rest.model.user.UserInformationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mealtiger.backend.SampleSource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class UserControllerTest {

    private RecipeRepository recipeRepository;
    private ImageMetadataRepository imageMetadataRepository;
    private UserMetadataRepository userMetadataRepository;
    private UserController userController;

    @BeforeEach
    @AfterEach
    void beforeAfterEach() {
        recipeRepository = mock(RecipeRepository.class);
        imageMetadataRepository = mock(ImageMetadataRepository.class);
        userMetadataRepository = mock(UserMetadataRepository.class);
        userController = spy(new UserController(userMetadataRepository, recipeRepository, imageMetadataRepository));
    }

    /**
     * Tests the getUserInformation method
     */
    @Test
    void getUserInformationTest() {
        UserMetadata userMetadata = new UserMetadata(SAMPLE_USER_ID, "Sample username", SAMPLE_IMAGE_ID);

        when(userMetadataRepository.findById(SAMPLE_USER_ID))
                .thenReturn(Optional.of(userMetadata));

        UserInformationResponse response = (UserInformationResponse) userController.getUserInformation(SAMPLE_USER_ID);
        verify(userMetadataRepository).findById(SAMPLE_USER_ID);

        assertEquals(userMetadata.getUserId(), response.getUserId());
        assertEquals(userMetadata.getUsername(), response.getUsername());
        assertEquals(userMetadata.getProfilePictureId(), response.getProfilePictureId());

        assertThrows(EntityNotFoundException.class, () -> userController.getUserInformation(SAMPLE_OTHER_USER_ID));
    }

    /**
     * Tests the createProfile method
     */
    @Test
    void createProfileTest() {
        UserInformationRequest userInformationRequest = new UserInformationRequest("Sample username", SAMPLE_IMAGE_ID);
        UserMetadata userMetadata = userInformationRequest.toEntity();
        userMetadata.setUserId(SAMPLE_USER_ID);

        when(userMetadataRepository.save(any())).thenReturn(userMetadata);

        UserInformationResponse response = (UserInformationResponse) userController.createProfile(SAMPLE_USER_ID, userInformationRequest);
        verify(userMetadataRepository).save(userMetadata);

        assertEquals(userMetadata.getUserId(), response.getUserId());
        assertEquals(userMetadata.getUsername(), response.getUsername());
        assertEquals(userMetadata.getProfilePictureId(), response.getProfilePictureId());
    }

    /**
     * Tests the editProfile method
     */
    @Test
    void editProfileTest() {
        UserInformationRequest userInformationRequest = new UserInformationRequest("Sample username", SAMPLE_IMAGE_ID);
        UserMetadata userMetadata = userInformationRequest.toEntity();
        userMetadata.setUserId(SAMPLE_USER_ID);

        when(userMetadataRepository.save(any())).thenReturn(userMetadata);

        UserInformationResponse response = (UserInformationResponse) userController.editProfile(SAMPLE_USER_ID, userInformationRequest);

        verify(userMetadataRepository).deleteById(SAMPLE_USER_ID);
        verify(userController).createProfile(SAMPLE_USER_ID, userInformationRequest);

        assertEquals(userMetadata.getUserId(), response.getUserId());
        assertEquals(userMetadata.getUsername(), response.getUsername());
        assertEquals(userMetadata.getProfilePictureId(), response.getProfilePictureId());
    }

    /**
     * Tests the doesProfileExist method
     */
    @Test
    void doesProfileExistTest() {
        when(userMetadataRepository.existsById(any())).thenReturn(false);
        when(userMetadataRepository.existsById(SAMPLE_USER_ID)).thenReturn(true);

        assertTrue(userController.doesProfileExist(SAMPLE_USER_ID));
        assertFalse(userController.doesProfileExist(SAMPLE_OTHER_USER_ID));
        verify(userMetadataRepository, times(2)).existsById(any());
    }

    /**
     * Tests the getRecipesByUserId method
     */
    @Test
    void getRecipesByUserIdTest() {
        @SuppressWarnings("unchecked")
        Page<Recipe> recipePage = (Page<Recipe>) mock(Page.class);
        @SuppressWarnings("unchecked")
        Page<Object> recipeResponsePage = (Page<Object>) mock(Page.class);
        when(recipePage.map(any())).thenReturn(recipeResponsePage);
        when(recipeResponsePage.getContent()).thenReturn(Arrays.stream(getSampleRecipes().stream().map(Recipe::toResponse).toArray()).toList());
        when(recipeResponsePage.getNumber()).thenReturn(0);
        when(recipeResponsePage.getTotalElements()).thenReturn(24L);
        when(recipeResponsePage.getTotalPages()).thenReturn(2);

        when(recipeRepository.findRecipesByUserId(eq(SAMPLE_USER_ID), any())).thenReturn(recipePage);

        Map<String, Object> response = userController.getRecipesByUserId(SAMPLE_USER_ID, "title", 12, 0);

        verify(recipeRepository).findRecipesByUserId(eq(SAMPLE_USER_ID), any());

        assertTrue(response.containsKey("recipes"));
        assertTrue(response.containsKey("currentPage"));
        assertTrue(response.containsKey("totalItems"));
        assertTrue(response.containsKey("totalPages"));

        assertEquals(0, response.get("currentPage"));
        assertEquals(24L, response.get("totalItems"));
        assertEquals(2, response.get("totalPages"));
        @SuppressWarnings("unchecked")
        List<RecipeResponse> responseList = (List<RecipeResponse>) response.get("recipes");
        assertEquals(SampleSource.getSampleRecipes().size(), responseList.size());
    }

    /**
     * Tests the getImagesByUserId method
     */
    @Test
    void getImagesByUserIdTest() {
        when(imageMetadataRepository.findImageMetadatasByUserId(SAMPLE_USER_ID))
                .thenReturn(List.of(new ImageMetadata(SAMPLE_IMAGE_ID, SAMPLE_USER_ID)));

        List<String> response = userController.getImagesByUserId(SAMPLE_USER_ID);

        verify(imageMetadataRepository).findImageMetadatasByUserId(SAMPLE_USER_ID);

        assertEquals(List.of(SAMPLE_IMAGE_ID), response);

        assertThrows(EntityNotFoundException.class, () -> userController.getImagesByUserId(SAMPLE_OTHER_USER_ID));
    }
}
