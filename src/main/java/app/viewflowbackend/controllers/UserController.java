package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.user.UserProfileDTO;
import app.viewflowbackend.DTO.user.UserUpdateRequestDTO;
import app.viewflowbackend.DTO.user.ViewerResponseDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.UserService;
import jakarta.validation.Valid;
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
        UserProfileDTO userProfile = userService.convertToUserProfileDTO(viewer);
        userProfile.setLikesCount(userService.getTotalReceivedLikes(viewer.getId()));
        userProfile.setCompilationsCount(userService.getTotalCreatedCompilations(viewer.getId()));
        userProfile.setFilmsWatched(userService.getTotalWatchedMedia(viewer.getId()));

        return ResponseEntity.ok(userProfile);
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateProfile(@CurrentUser Viewer viewer, @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) {
        userService.updateProfile(viewer, userUpdateRequestDTO);
        return ResponseEntity.ok("Successfully updated");
    }

    @GetMapping("/info")
    public ResponseEntity<ViewerResponseDTO> getCurrentViewer(@CurrentUser Viewer viewer) {
        ViewerResponseDTO dto = ViewerResponseDTO.builder()
                .id(viewer.getId())
                .username(viewer.getUsername())
                .build();
        return ResponseEntity.ok(dto);
    }
}
