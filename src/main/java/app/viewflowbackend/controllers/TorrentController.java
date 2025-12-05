package app.viewflowbackend.controllers;

import app.viewflowbackend.DTO.api.TorrentOptionDTO;
import app.viewflowbackend.services.api.TorrentParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torrents")
public class TorrentController {

    private final TorrentParserService torrentParserService;

    @Autowired
    public TorrentController(TorrentParserService torrentParserService) {
        this.torrentParserService = torrentParserService;
    }

    @GetMapping("/movie")
    public ResponseEntity<Map<String, List<TorrentOptionDTO>>> getMovieTorrents(@RequestParam String query) {
        List<TorrentOptionDTO> allTorrents = torrentParserService.getMovieTorrents(query);

        Map<String, List<TorrentOptionDTO>> grouped = allTorrents.stream()
                .collect(Collectors.groupingBy(TorrentOptionDTO::getResolution));

        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/series")
    public ResponseEntity<Map<String, List<TorrentOptionDTO>>> getSeriesTorrents(
            @RequestParam String query,
            @RequestParam Integer season) {

        List<TorrentOptionDTO> seasonTorrents = torrentParserService.getSeriesSeasonTorrents(query, season);

        Map<String, List<TorrentOptionDTO>> grouped = seasonTorrents.stream()
                .collect(Collectors.groupingBy(TorrentOptionDTO::getResolution));

        return ResponseEntity.ok(grouped);
    }
}