package app.viewflowbackend.services.api;

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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TmdbService {
    private final RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    @Autowired
    public TmdbService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MediaDetailsDTO getMediaDetails(Long id, MediaType type) {
        // TODO: Make request to Redis

        String url = "https://api.themoviedb.org/3/" + type.name().toLowerCase() + "/" + id + "?api_key=" + apiKey + "&language=ru-RU";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new MediaNotFoundException(id, type);
        }

        Map data = response.getBody();
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
                .genres(((List<Map>) data.get("genres")).stream().map(g -> (String) g.get("name")).collect(Collectors.toList()))
                .runtime(data.get("runtime") != null ?
                        (Integer) data.get("runtime") :
                        (data.get("episode_run_time") != null && !((List<Integer>) data.get("episode_run_time")).isEmpty() ?
                                ((List<Integer>) data.get("episode_run_time")).get(0) : null))
                .numberOfSeasons(type == MediaType.TV ? (Integer) data.get("number_of_seasons") : null)
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

    //TODO: add another methods
}
