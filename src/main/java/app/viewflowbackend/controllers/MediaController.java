package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.api.*;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.DTO.auxiliary.MediaSelectionDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.enums.RandomType;
import app.viewflowbackend.services.api.KinopoiskService;
import app.viewflowbackend.services.api.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final TmdbService tmdbService;
    private final KinopoiskService kinopoiskService;


    @Autowired
    public MediaController(TmdbService tmdbService, KinopoiskService kinopoiskService) {
        this.tmdbService = tmdbService;
        this.kinopoiskService = kinopoiskService;
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

    @GetMapping("/genres")
    public ResponseEntity<List<GenreDTO>> getGenres() {
        return ResponseEntity.ok(tmdbService.getGenres());
    }


    @GetMapping("/random")
    public ResponseEntity<RandomMediaResponseCardDTO> getRandomMedia(@RequestParam(required = false) Long genreId,
                                                                     @RequestParam Integer minYear,
                                                                     @RequestParam Integer maxYear,
                                                                     @RequestParam Double minRating,
                                                                     @RequestParam Double maxRating,
                                                                     @RequestParam RandomType randomType) {
        RandomMediaCardRequestDTO dto;

        if (genreId != null && genreId > 0) {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setId(genreId);
            dto = new RandomMediaCardRequestDTO(genreDTO, minYear, maxYear, minRating, maxRating, randomType);
        } else {
            dto = new RandomMediaCardRequestDTO(null, minYear, maxYear, minRating, maxRating, randomType);
        }

        for (int i = 0; i < 20; i++) {
            RandomMediaResponseCardDTO card = kinopoiskService.getRandomMediaCard(dto);
            if (card != null) {
                return ResponseEntity.ok(card);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/selection")
    public ResponseEntity<List<MediaSelectionDTO>> getSelectionMedia(@RequestParam String query) {
        return ResponseEntity.ok(tmdbService.getMediaSelection(query));
    }
}
