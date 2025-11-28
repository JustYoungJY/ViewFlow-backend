package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.RatingResponseDTO;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.notFound.MediaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TmdbService {

    private final RestTemplate restTemplate;
    private final KinopoiskService kinopoiskService;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Autowired
    public TmdbService(RestTemplate restTemplate, KinopoiskService kinopoiskService) {
        this.restTemplate = restTemplate;
        this.kinopoiskService = kinopoiskService;
    }

    public MediaDetailsDTO getMediaDetails(Long id, MediaType type) {
        // TODO: Make request to Redis

        String url = "https://api.themoviedb.org/3/" + type.name().toLowerCase() + "/" + id + "?api_key=" + apiKey
                + "&append_to_response=external_ids,credits,videos" + "&language=ru-RU";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new MediaNotFoundException(id, type);
        }

        Map data = response.getBody();

        RatingResponseDTO kinopoiskRatings = null;
        if (data.containsKey("external_ids")) {

            Map<String, Object> externalIds = (Map<String, Object>) data.get("external_ids");
            String imdbId = (String) externalIds.get("imdb_id");
            try {
                if (imdbId != null && !imdbId.isEmpty()) {
                    kinopoiskRatings = kinopoiskService.getRating(imdbId);
                }
            } catch (Exception e) {
                //TODO: add log
            }
        }

        Map<String, Object> creditsMap = (Map<String, Object>) data.get("credits");
        List<String> directors = new ArrayList<>();
        if (creditsMap != null) {
            List<Map<String, Object>> crewList = (List<Map<String, Object>>) creditsMap.get("crew");

            if (crewList != null) {
                for (Map<String, Object> crewMember : crewList) {

                    String job = (String) crewMember.get("job");

                    if ("Director".equals(job)) {

                        String directorName = (String) crewMember.get("name");
                        directors.add(directorName);
                    }
                }
            }
        }


        String trailerYoutubeId = null;
        if (data.containsKey("videos")) {
            Map videosMap = (Map) data.get("videos");
            List<Map<String, Object>> results = (List<Map<String, Object>>) videosMap.get("results");

            if (results != null && !results.isEmpty()) {
                Optional<Map<String, Object>> bestTrailer = results.stream()
                        .filter(video -> "Trailer".equalsIgnoreCase((String) video.get("type")))
                        .filter(video -> "YouTube".equalsIgnoreCase((String) video.get("site")))
                        .findFirst();

                if (bestTrailer.isPresent()) {
                    trailerYoutubeId = (String) bestTrailer.get().get("key");
                }
            }
        }


        MediaDetailsDTO dto = MediaDetailsDTO
                .builder()
                .tmdbId(id)
                .mediaType(type)
                .title(data.get("title") != null ? (String) data.get("title") : (String) data.get("name"))
                .originalTitle(data.get("original_title") != null ? (String) data.get("original_title") : (String) data.get("original_name"))
                .posterPath((String) data.get("poster_path"))
                .backdropPath((String) data.get("backdrop_path"))
                .releaseYear(Optional.ofNullable((String) data.get("release_date"))
                        .orElse(Optional.ofNullable((String) data.get("first_air_date")).orElse("")).length() >= 4 ?
                        Integer.parseInt(Optional.ofNullable((String) data.get("release_date"))
                                .orElse((String) data.get("first_air_date")).substring(0, 4)) : null)
                .overview((String) data.get("overview"))
                .voteAverage((Double) data.get("vote_average"))
                .voteCount((Integer) data.get("vote_count"))
                .trailerYoutubeId(trailerYoutubeId)
                .genres(((List<Map>) data.get("genres")).stream().map(g -> (String) g.get("name")).collect(Collectors.toList()))
                .runtime(data.get("runtime") != null ?
                        (Integer) data.get("runtime") :
                        (data.get("episode_run_time") != null && !((List<Integer>) data.get("episode_run_time")).isEmpty() ?
                                ((List<Integer>) data.get("episode_run_time")).get(0) : null))
                .numberOfSeasons(type == MediaType.TV ? (Integer) data.get("number_of_seasons") : null)
                .country(
                        data.get("production_countries") != null && !((List<Map>) data.get("production_countries")).isEmpty() ?
                                (String) ((List<Map>) data.get("production_countries")).get(0).get("name") : "Неизвестно"
                )
                .budget(
                        data.get("budget") != null && (Integer) data.get("budget") > 0 ?
                                String.format("$%,d", (Integer) data.get("budget")) : "Неизвестно"
                )
                .directors(directors)
                .ratings(kinopoiskRatings)
                .build();

        return dto;
    }


    public void checkMediaExists(Long id, MediaType type) {

        String url = "https://api.themoviedb.org/3/" + type.name().toLowerCase() + "/" + id + "?api_key=" + apiKey + "&language=ru-RU";

        try {
            restTemplate.getForEntity(url, Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new MediaNotFoundException(id, type);
            }
            throw e;
        }
    }

    // TODO: Write method for searching cast


    //TODO: add another methods
}
