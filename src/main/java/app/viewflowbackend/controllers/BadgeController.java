package app.viewflowbackend.controllers;


import app.viewflowbackend.DTO.badge.BadgeCreateRequestDTO;
import app.viewflowbackend.DTO.badge.BadgeResponseDTO;
import app.viewflowbackend.DTO.badge.BadgeUpdateRequestDTO;
import app.viewflowbackend.annotations.CurrentUser;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.models.basic.Viewer;
import app.viewflowbackend.services.BadgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;


    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createBadge(@CurrentUser Viewer viewer,
                                            @Valid @RequestBody BadgeCreateRequestDTO dto) {
        badgeService.createBadge(viewer, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/media")
    public ResponseEntity<BadgeResponseDTO> getBadge(@RequestParam Long mediaId,
                                                     @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(badgeService.getMediaBadge(mediaId, mediaType));
    }


    @GetMapping
    public ResponseEntity<List<BadgeResponseDTO>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{badgeId}")
    public ResponseEntity<Void> updateBadge(@CurrentUser Viewer viewer, @PathVariable Long badgeId,
                                            @Valid @RequestBody BadgeUpdateRequestDTO dto) {
        badgeService.updateBadge(viewer, badgeId, dto);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{badgeId}")
    public ResponseEntity<Void> deleteBadge(@CurrentUser Viewer viewer, @PathVariable Long badgeId) {
        badgeService.deleteBadge(viewer, badgeId);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/media/add")
    public ResponseEntity<Void> addBadgeToMedia(@CurrentUser Viewer viewer,
                                                @RequestParam Long badgeId,
                                                @RequestParam Long mediaId,
                                                @RequestParam MediaType mediaType) {
        badgeService.addBadgeToMedia(viewer, badgeId, mediaId, mediaType);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/media/remove")
    public ResponseEntity<Void> removeBadgeFromMedia(@CurrentUser Viewer viewer,
                                                     @RequestParam Long mediaId,
                                                     @RequestParam MediaType mediaType) {
        badgeService.removeBadgeFromMedia(viewer, mediaId, mediaType);
        return ResponseEntity.ok().build();
    }


}
