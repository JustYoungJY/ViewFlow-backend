package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.MediaRatingResponseDTO;
import app.viewflowbackend.exceptions.api.InvalidResponseFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class KinopoiskService {

    private final RestTemplate restTemplate;

    @Value("${kinopoisk.api.key}")
    private String apiKey;

    @Value("${kinopoisk.api.baseurl}")
    private String baseUrl;


    @Autowired
    public KinopoiskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            return items.stream().map(item -> ((Number) item.get("filmId")).longValue()).limit(10).toList();

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
}
