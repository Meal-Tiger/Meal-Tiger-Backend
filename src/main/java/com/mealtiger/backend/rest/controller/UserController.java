package com.mealtiger.backend.rest.controller;

import com.mealtiger.backend.database.model.image_metadata.ImageMetadata;
import com.mealtiger.backend.database.model.recipe.Recipe;
import com.mealtiger.backend.database.model.user.UserMetadata;
import com.mealtiger.backend.database.repository.ImageMetadataRepository;
import com.mealtiger.backend.database.repository.RecipeRepository;
import com.mealtiger.backend.database.repository.UserMetadataRepository;
import com.mealtiger.backend.rest.error_handling.exceptions.EntityNotFoundException;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.user.UserInformationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserController {

    private final RecipeRepository recipeRepository;
    private final ImageMetadataRepository imageMetadataRepository;
    private final UserMetadataRepository userMetadataRepository;

    public UserController(UserMetadataRepository userMetadataRepository,
                          RecipeRepository recipeRepository,
                          ImageMetadataRepository imageMetadataRepository) {
        this.imageMetadataRepository = imageMetadataRepository;
        this.recipeRepository = recipeRepository;
        this.userMetadataRepository = userMetadataRepository;
    }

    /**
     * Gets the user information from the repository.
     * @param userId The id of the user.
     * @return User information.
     */
    public Response getUserInformation(String userId) {
        return getUserInformationFromRepository(userId);
    }

    /**
     * Creates user Profile.
     * @param userId The id of the user.
     * @param userInformationRequest Information about the user.
     * @return Saved user data as response.
     */
    public Response createProfile(String userId, UserInformationRequest userInformationRequest) {
        UserMetadata userMetadata = userInformationRequest.toEntity();
        userMetadata.setUserId(userId);

        return userMetadataRepository.save(userMetadata).toResponse();
    }

    /**
     * Edits user Profile.
     * @param userId The id of the user.
     * @param userInformationRequest Information about the user.
     * @return saved User data as response.
     */
    public Response editProfile(String userId, UserInformationRequest userInformationRequest) {
        userMetadataRepository.deleteById(userId);
        return createProfile(userId, userInformationRequest);
    }

    /**
     * Checks if Profile does Exist.
     * @param userId The id of the user.
     * @return True, if present, and false, if not.
     */
    public boolean doesProfileExist(String userId) {
        return userMetadataRepository.existsById(userId);
    }

    /**
     * Gets Recipes by a specific userid.
     * @param userId The id of the user.
     * @param page Int of Page we are on.
     * @param size Int of Page size.
     * @param sort String to sort after.
     * @return Sorted and paginated recipes from the repository belonging to userid as a map.
     */
    public Map<String, Object> getRecipesByUserId(String userId, String sort, int size, int page) {
        Page<Recipe> recipePage = getRecipesByUserIdFromRepository(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort)));

        return assemblePaginatedResult(recipePage.map(Recipe::toResponse), "recipes");
    }

    /**
     * Gets images by a specific userid.
     * @param userId The id of the user.
     * @return List of Images belonging to userid.
     */
    public List<String> getImagesByUserId(String userId) {
        return getImageMetadataByUserIdFromRepository(userId).stream().map(ImageMetadata::getId).toList();
    }

    /**
     * Gets User Information by a specific userid.
     * @param userId The id of the user.
     * @return The User information or if nor exists EntityNotFoundExeption.
     */
    private Response getUserInformationFromRepository(String userId) {
        return userMetadataRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id " +
                                userId + " does not exist yet or has not configured its profile.")
        ).toResponse();
    }

    /**
     * Gets Recipes of a specific userid.
     * @param userId The id of the user.
     * @param pageable pagable object that makes the page paginated.
     * @return A Recipe Page.
     */
    private Page<Recipe> getRecipesByUserIdFromRepository(String userId, Pageable pageable) {
        return recipeRepository.findRecipesByUserId(userId, pageable);
    }

    /**
     * Gets Images of a specific userid.
     * @param userId The id of the user.
     * @return List of Images belonging to userid.*
     */
    private List<ImageMetadata> getImageMetadataByUserIdFromRepository(String userId) {
        List<ImageMetadata> imageMetadata = imageMetadataRepository.findImageMetadatasByUserId(userId);
        if (imageMetadata.isEmpty()) throw new EntityNotFoundException("The user with id " + userId + " doesn't own any images.");
        return imageMetadata;
    }

    /**
     * This method assembles a map that contains all needed information for a stateless implementation in the frontend application from a page.
     *
     * @param page Page to be used for assembling the result.
     * @return Map that contains the entries objectName, 'currentPage', 'totalItems', 'totalPages'.
     */
    private Map<String, Object> assemblePaginatedResult(Page<Response> page, String objectName) {
        List<?> objects = page.getContent();

        if (objects.isEmpty()) {
            throw new EntityNotFoundException("No items for page " + page.getNumber() + " yet!");
        }

        Map<String, Object> response = new HashMap<>();
        response.put(objectName, objects);
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }

}
