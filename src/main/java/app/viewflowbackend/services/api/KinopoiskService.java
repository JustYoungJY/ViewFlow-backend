package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.MediaRatingResponseDTO;
import app.viewflowbackend.DTO.api.RandomMediaCardRequestDTO;
import app.viewflowbackend.DTO.api.RandomMediaResponseCardDTO;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.DTO.auxiliary.TmdbMediaIdDTO;
import app.viewflowbackend.exceptions.api.InvalidResponseFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class KinopoiskService {

    private final RestTemplate restTemplate;
    private final TmdbService tmdbService;

    @Value("${kinopoisk.api.key}")
    private String apiKey;

    @Value("${kinopoisk.api.baseurl}")
    private String baseUrl;


    @Autowired
    public KinopoiskService(RestTemplate restTemplate,@Lazy TmdbService tmdbService) {
        this.restTemplate = restTemplate;
        this.tmdbService = tmdbService;
    }


    public MediaRatingResponseDTO getRating(String imdbId) {
        // TODO: Make request to Redis

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/v2.2/films?imdbId=" + imdbId + "&page=1";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            Map data = response.getBody();

            if (data == null || !data.containsKey("items")) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

            if (items == null || items.isEmpty()) {
                throw new InvalidResponseFormatException("Фильм с IMDb ID" + imdbId + " не найден");
            }

            Map<String, Object> filmData = items.get(0);
            Double ratingKinopoisk = Optional.ofNullable((Double) filmData.get("ratingKinopoisk")).orElse(0.0);
            Double ratingImdb = Optional.ofNullable((Double) filmData.get("ratingImdb")).orElse(0.0);

            return MediaRatingResponseDTO
                    .builder()
                    .ratingKinopoisk(ratingKinopoisk)
                    .ratingImdb(ratingImdb)
                    .build();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException("Ошибка при получении данных от Kinopoisk API: " + e.getStatusCode());
        }
    }


    public List<Long> getSimilarsMediaIds(Long kinopoiskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/v2.2/films/" + kinopoiskId + "/similars";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            Map data = response.getBody();

            if (data == null || !data.containsKey("items")) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
            return items.stream().map(item -> ((Number) item.get("filmId")).longValue())
                    .limit(2).toList();  // Count of movies

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException("Ошибка при получении данных от Kinopoisk API: " + e.getStatusCode());
        }
    }


    public Long getKinopoiskIdByImdbId(String imdbId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = baseUrl + "/api/v2.2/films?imdbId=" + imdbId + "&page=1";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            Map data = response.getBody();

            if (data == null || !data.containsKey("items")) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");

            if (items == null || items.isEmpty()) {
                throw new InvalidResponseFormatException("Фильм с IMDb ID" + imdbId + " не найден");
            }

            Map<String, Object> filmData = items.get(0);

            return ((Number) filmData.get("kinopoiskId")).longValue();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException("Ошибка при получении данных от Kinopoisk API: " + e.getStatusCode());
        }
    }


    public String getImdbIdByKinopoiskId(Long kinopoiskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //https://kinopoiskapiunofficial.tech/api/v2.2/films/9324793
        String url = baseUrl + "/api/v2.2/films/" + kinopoiskId;

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map data = response.getBody();

            if (data == null) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            return (String) data.get("imdbId");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException(e.getMessage());
        }
    }


    public String getPoster(Long kinopoiskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        //https://kinopoiskapiunofficial.tech/api/v2.2/films/361/images?type=WALLPAPER&page=1
        String url = baseUrl + "/api/v2.2/films/" + kinopoiskId + "/images?type=WALLPAPER&page=1";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            Map data = response.getBody();
            if (data == null || !data.containsKey("items")) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
            return (String) items.get(0).get("imageUrl");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException(e.getMessage());
        }
    }


    public RandomMediaResponseCardDTO getRandomMediaCard(RandomMediaCardRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "";

        //https://kinopoiskapiunofficial.tech/api/v2.2/films?genres=13&order=RATING&type=FILM&ratingFrom=0&ratingTo=10&yearFrom=0&yearTo=3000&page=1
        if(request.getGenre() != null) {
            url = baseUrl + "/api/v2.2/films?genres=" + request.getGenre().getId() + "&order=RATING&type="
                    + request.getRandomType() +"&ratingFrom=" + request.getMinRating() + "&ratingTo=" + request.getMaxRating()
                    + "&yearFrom=" + request.getMinYear() + "&yearTo=" + request.getMaxYear() + "&page=1";
        } else {
            url = baseUrl + "/api/v2.2/films?order=RATING&type=" + request.getRandomType() +"&ratingFrom="
                    + request.getMinRating() + "&ratingTo=" + request.getMaxRating() + "&yearFrom="
                    + request.getMinYear() + "&yearTo=" + request.getMaxYear() + "&page=1";
        }

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map data = response.getBody();

            if (data == null) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");
            }

            Integer totalPage = (Integer) data.get("totalPages");
            Integer total = (Integer) data.get("total");
            if(total == null || total == 0) {
                return null;
            }

            Random random = new Random();
            url = url.substring(0, url.length() - 1) + (random.nextInt(totalPage) + 1);

            ResponseEntity<Map> secondResponse = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map secondData = secondResponse.getBody();

            if (secondData == null) {
                throw new InvalidResponseFormatException("Неверный формат ответа от Kinopoisk API");

            }

            List<Map<String, Object>> items = (List<Map<String, Object>>) secondData.get("items");
            if (items == null || items.isEmpty()) {
                return null;
            }

            Integer randomListIndex = random.nextInt(items.size());
            Map<String, Object> item = items.get(randomListIndex);
            Number kinopoiskId = (Number) item.get("kinopoiskId");
            String imdbItemId = getImdbIdByKinopoiskId(kinopoiskId.longValue());
            TmdbMediaIdDTO tmdbMediaIdDTO = tmdbService.getTmdbMediaIdAndMediaTypeByImdbId(imdbItemId);


            MediaDetailsDTO mediaDetailsDTO = tmdbService.getMediaDetails(tmdbMediaIdDTO.getMediaId(), tmdbMediaIdDTO.getMediaType());

            return RandomMediaResponseCardDTO
                    .builder()
                    .mediaId(tmdbMediaIdDTO.getMediaId())
                    .mediaType(tmdbMediaIdDTO.getMediaType())
                    .title(mediaDetailsDTO.getTitle())
                    .description(mediaDetailsDTO.getOverview())
                    .year(mediaDetailsDTO.getReleaseYear())
                    .rating(mediaDetailsDTO.getVoteAverage())
                    .posterUrl(mediaDetailsDTO.getPosterPath())
                    .genres(mediaDetailsDTO.getGenres())
                    .build();
        } catch (NullPointerException e) {
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException(e.getMessage());
        }
    }
}
