package com.mealtiger.backend.rest.api;

import com.mealtiger.backend.rest.controller.UserController;
import com.mealtiger.backend.rest.model.Response;
import com.mealtiger.backend.rest.model.user.UserInformationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class UserAPI {

    private final UserController userController;

    public UserAPI(UserController userController) {
        this.userController = userController;
    }

    /**
     * Gets own user information.
     * @return Response Entity (Status code 200) with the response in its body.
     */
    @GetMapping("/user")
    public ResponseEntity<Response> getOwnUserInformation() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(userController.getUserInformation(userId));
    }

    /**
     * Post new user information.
     * @param userInformationRequest Information of the user.
     * @return Response Entity (Status code 201) with the saved user information in its body and location in the location header.
     */
    @PostMapping("/user")
    public  ResponseEntity<Response> postUserInformation(@RequestBody @Valid UserInformationRequest userInformationRequest){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.created(URI.create("/user/" + userId)).body(userController.createProfile(userId, userInformationRequest));
    }

    /**
     * Change user information.
     * @param userInformationRequest Information of the user.
     * @return Response entity (status code 200) with the response in its body, if already present, if not Response entity (status code 201) with the stored user information in its body and the location in the Location header.
     */
    @PutMapping("/user")
    public  ResponseEntity<Response> putUserInformation(@RequestBody @Valid UserInformationRequest userInformationRequest){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userController.doesProfileExist(userId)) {
            return ResponseEntity.ok(userController.editProfile(userId, userInformationRequest));
        } else {
            return ResponseEntity.created(URI.create("/user/" + userId)).body(userController.createProfile(userId, userInformationRequest));
        }
    }

    /**
     * Gets own recipes.
     * @param page  # of current page, default is 0.
     * @param size  page size, default is 3.
     * @param sort  string to sort after, default is title.
     * @return Response Entity (Status code 200) with the response map in its body.
     */
    @GetMapping("/user/recipes")
    public ResponseEntity<Map<String, Object>> getOwnRecipes(@RequestParam(value = "sort", defaultValue = "title") String sort,
                                                             @RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "3") int size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(userController.getRecipesByUserId(userId, sort, size, page));
    }

    /**
     * Gets own images.
     * @return Response Entity (Status code 200) with the response in its body.
     */
    @GetMapping("/user/images")
    public ResponseEntity<List<String>> getOwnImages(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(userController.getImagesByUserId(userId));
    }

    /**
     * Gets information about a user.
     * @param userId Id of a user.
     * @return Response Entity (Status code 200) with the response in its body.
     */
    @GetMapping("/user/{userid}")
    public ResponseEntity<Response> getUserInformation(@PathVariable(value = "userid") String userId){
        return ResponseEntity.ok(userController.getUserInformation(userId));
    }

    /**
     * Gets recipes from specific user.
     * @param userId Id of a user.
     * @param page  # of current page, default is 0.
     * @param size  page size, default is 3.
     * @param sort  string to sort after, default is title.
     * @return Response Entity (Status code 200) with the response map in its body.
     */
    @GetMapping("/user/{userid}/recipes")
    public ResponseEntity<Map<String, Object>> getUserRecipes(@PathVariable(value = "userid") String userId,
                                                   @RequestParam(value = "sort", defaultValue = "title") String sort,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "3") int size){
        return ResponseEntity.ok(userController.getRecipesByUserId(userId, sort, size, page));
    }

    /**
     * Gets the images of a specific user.
     * @param userId Id of a user.
     * @return Response Entity (Status code 200) with the response map in its body.
     */
    @GetMapping("/user/{userid}/images")
    public ResponseEntity<List<String>> getUserImages(@PathVariable(value = "userid") String userId){
        return ResponseEntity.ok(userController.getImagesByUserId(userId));
    }
}
