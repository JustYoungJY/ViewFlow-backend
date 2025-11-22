package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.user.UserProfileDTO;
import app.viewflowbackend.DTO.user.UserUpdateRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getProfile(@CurrentUser Viewer viewer) {
        // TODO: Make request to fill in the fields:
        //  likesCount, compilationsCount, filmsWatched
        return ResponseEntity.ok(userService.convertToUserProfileDTO(viewer));
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateProfile(@CurrentUser Viewer viewer, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        userService.updateProfile(viewer, userUpdateRequestDTO);
        return ResponseEntity.ok("Successfully updated");
    }
}
