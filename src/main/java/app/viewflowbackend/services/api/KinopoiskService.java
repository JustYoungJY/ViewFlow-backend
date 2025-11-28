package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.RatingResponseDTO;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    public RatingResponseDTO getRating(String imdbId) {
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
            Double ratingKinopoisk = (Double) filmData.get("ratingKinopoisk");
            Double ratingImdb = (Double) filmData.get("ratingImdb");

            return new RatingResponseDTO(ratingKinopoisk, ratingImdb);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException("Ошибка при получении данных от Kinopoisk API: " + e.getStatusCode());
        }
    }
}
