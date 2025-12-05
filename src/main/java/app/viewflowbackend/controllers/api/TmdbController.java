package app.viewflowbackend.controllers.api;

import app.viewflowbackend.DTO.api.TmdbWatchProviderDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.services.api.TmdbParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbParserService tmdbParserService;

    @Autowired
    public TmdbController(TmdbParserService tmdbParserService) {
        this.tmdbParserService = tmdbParserService;
    }

    @GetMapping("/providers")
    public ResponseEntity<TmdbWatchProviderDTO> getProviders(
            @RequestParam Long mediaId,
            @RequestParam MediaType mediaType
    ) {
        TmdbWatchProviderDTO result = tmdbParserService.getWatchProviders(mediaId, mediaType);

        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.noContent().build();
    }
}