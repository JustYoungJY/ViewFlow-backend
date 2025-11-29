package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.api.MediaCardResponseDTO;
import app.viewflowbackend.DTO.api.MediaCarouselResponseDTO;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final TmdbService tmdbService;


    @Autowired
    public MediaController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }


    @GetMapping
    public ResponseEntity<MediaDetailsDTO> getMedia(@RequestParam Long mediaId, @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(tmdbService.getMediaDetails(mediaId, mediaType));
    }


    @GetMapping("/similar")
    public ResponseEntity<List<MediaCardResponseDTO>> getSimilarMedia(@RequestParam Long mediaId, @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(tmdbService.getSimilarsMediaCard(mediaId, mediaType));
    }


    @GetMapping("/now-playing")
    public ResponseEntity<List<MediaCarouselResponseDTO>> getNowPlayingMedia() {
        return ResponseEntity.ok(tmdbService.getNowPlayingMedia());
    }
}
