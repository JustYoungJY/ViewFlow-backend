package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.favoriteMedia.FavoriteMediaAddRequestDTO;
import app.viewflowbackend.DTO.favoriteMedia.FavoriteMediaResponseDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.favorite.FavoriteMediaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites/media")
public class FavoriteMediaController {

    private final FavoriteMediaService favoriteMediaService;

    @Autowired
    public FavoriteMediaController(FavoriteMediaService favoriteMediaService) {
        this.favoriteMediaService = favoriteMediaService;
    }


    @PostMapping
    public ResponseEntity<Void> toggleFavoriteMedia(@CurrentUser Viewer viewer,
                                                    @Valid @RequestBody FavoriteMediaAddRequestDTO dto) {

        favoriteMediaService.toggleFavoriteMedia(viewer, dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/status")
    public ResponseEntity<Boolean> getFavorite(@CurrentUser Viewer viewer,
                                               @RequestParam Long mediaId,
                                               @RequestParam MediaType mediaType
    ) {
        try {
            favoriteMediaService.getFavoriteMedia(viewer, mediaId, mediaType);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }


    @GetMapping
    public ResponseEntity<Page<FavoriteMediaResponseDTO>> getFavorites(@CurrentUser Viewer viewer,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(favoriteMediaService.getFavoritesMedia(viewer, pageable));
    }

}
