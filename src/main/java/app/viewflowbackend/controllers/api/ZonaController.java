package app.viewflowbackend.controllers.api;

import app.viewflowbackend.DTO.api.ZonaMovieDTO;
import app.viewflowbackend.services.api.ZonaParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zona")
public class ZonaController {

    private final ZonaParserService parserService;

    @Autowired
    public ZonaController(ZonaParserService parserService) {
        this.parserService = parserService;
    }

    @GetMapping
    public ResponseEntity<?> searchMovie(
            @RequestParam String title,
            @RequestParam Integer year
    ) {
        ZonaMovieDTO result = parserService.findMovie(title, year);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}