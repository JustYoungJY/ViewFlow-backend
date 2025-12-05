package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.*;
import app.viewflowbackend.DTO.auxiliary.MediaDetailsDTO;
import app.viewflowbackend.DTO.auxiliary.MediaSelectionDTO;
import app.viewflowbackend.DTO.auxiliary.TmdbMediaIdDTO;
import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.exceptions.api.InvalidResponseFormatException;
import app.viewflowbackend.exceptions.notFound.MediaNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TmdbService {

    private final RestTemplate restTemplate;
    private final KinopoiskService kinopoiskService;
    private final ObjectMapper objectMapper;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Autowired
    public TmdbService(RestTemplate restTemplate, KinopoiskService kinopoiskService, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.kinopoiskService = kinopoiskService;
        this.objectMapper = objectMapper;
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

        MediaRatingResponseDTO kinopoiskRatings = new MediaRatingResponseDTO();
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


        kinopoiskRatings.setRatingTmdb(Math.round(((Double) data.get("vote_average")) * 10.0) / 10.0);
        Double sumOfRatings = kinopoiskRatings.getRatingTmdb();
        int countOfRatings = 1;

        Double kinopoisk = kinopoiskRatings.getRatingKinopoisk();
        Double imdb = kinopoiskRatings.getRatingImdb();

        if (kinopoisk != 0.0) {
            sumOfRatings += kinopoisk;
            countOfRatings++;
        }
        if (imdb != 0.0) {
            sumOfRatings += imdb;
            countOfRatings++;
        }
        Double averageRating = Math.round((sumOfRatings / countOfRatings) * 10.0) / 10.0;


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
                .voteAverage(averageRating)
                .voteCount((Integer) data.get("vote_count"))
                .trailerYoutubeId(trailerYoutubeId)
                .genres(((List<Map>) data.get("genres")).stream().map(g -> (String) g.get("name")).collect(Collectors.toList()))
                .runtime(data.get("runtime") != null ?
                        (Integer) data.get("runtime") :
                        (data.get("episode_run_time") != null && !((List<Integer>) data.get("episode_run_time")).isEmpty() ?
                                ((List<Integer>) data.get("episode_run_time")).get(0) : null))
                .numberOfSeasons(type == MediaType.TV ? (Integer) data.get("number_of_seasons") : null)
                .numberOfEpisodes(type == MediaType.TV ? (Integer) data.get("number_of_episodes") : null)
                .country(
                        data.get("production_countries") != null && !((List<Map>) data.get("production_countries")).isEmpty() ?
                                (String) ((List<Map>) data.get("production_countries")).get(0).get("name") : ""
                )
                .budget(
                        data.get("budget") != null && (Integer) data.get("budget") > 0 ?
                                String.format("$%,d", (Integer) data.get("budget")) : ""
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


    public List<MediaCardResponseDTO> getSimilarsMediaCard(Long mediaId, MediaType mediaType) {
        String imdbId = getImdbId(mediaId, mediaType);
        Long kinopoiskId = kinopoiskService.getKinopoiskIdByImdbId(imdbId);
        List<Long> listSimilarIds = kinopoiskService.getSimilarsMediaIds(kinopoiskId);
        List<TmdbMediaIdDTO> ids = listSimilarIds.stream()
                .map(id -> getTmdbMediaIdAndMediaTypeByImdbId(kinopoiskService.getImdbIdByKinopoiskId(id)))
                .filter(id -> id.getMediaId() != null && id.getMediaType() != null)
                .toList();
        List<MediaDetailsDTO> listMediaDetails = ids.stream().map(id ->
                getMediaDetails(id.getMediaId(), id.getMediaType())).toList();

        return listMediaDetails.stream().map(detail ->
                MediaCardResponseDTO
                        .builder()
                        .mediaId(detail.getTmdbId())
                        .mediaType(detail.getMediaType())
                        .title(detail.getTitle())
                        .posterUrl(detail.getPosterPath())
                        .year(detail.getReleaseYear())
                        .genres(detail.getGenres())
                        .rating(detail.getVoteAverage())
                        .build()
        ).toList();

    }


    public String getImdbId(Long mediaId, MediaType mediaType) {
        String url = "https://api.themoviedb.org/3/" + mediaType.name().toLowerCase() + "/" + mediaId + "/external_ids?api_key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map data = response.getBody();

            if (!data.containsKey("imdb_id")) {
                throw new MediaNotFoundException(mediaId, mediaType);
            }

            return (String) data.get("imdb_id");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException("Ошибка при получении данных от TMDB API: " + e.getStatusCode());
        }
    }


    public TmdbMediaIdDTO getTmdbMediaIdAndMediaTypeByImdbId(String imdbId) {
        String url = "https://api.themoviedb.org/3/find/" + imdbId + "?api_key=" + apiKey + "&external_source=imdb_id";

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map data = response.getBody();

            if (!data.containsKey("movie_results") && !data.containsKey("tv_results")) {
                throw new InvalidResponseFormatException("Медиа с IMDb ID" + imdbId + " не найден");
            }

            if (data.containsKey("movie_results")) {
                List<Map<String, Object>> movieResults = (List<Map<String, Object>>) data.get("movie_results");

                if (!movieResults.isEmpty()) {
                    Map<String, Object> movieResult = movieResults.get(0);

//                Long mediaId = (Long) movieResult.get("id");
                    Number numberMediaId = (Number) movieResult.get("id");
                    Long mediaId = numberMediaId != null ? numberMediaId.longValue() : null;

                    MediaType mediaType = MediaType.valueOf(movieResult.get("media_type").toString().toUpperCase());
                    return new TmdbMediaIdDTO(mediaId, mediaType);
                }
            }

            if (data.containsKey("tv_results")) {
                List<Map<String, Object>> tvResults = (List<Map<String, Object>>) data.get("tv_results");

                if (!tvResults.isEmpty()) {
                    Map<String, Object> tvResult = tvResults.get(0);

//                Long mediaId = (Long) tvResult.get("id");
                    Number numberMediaId = (Number) tvResult.get("id");
                    Long mediaId = numberMediaId != null ? numberMediaId.longValue() : null;

                    MediaType mediaType = MediaType.valueOf(tvResult.get("media_type").toString().toUpperCase());
                    return new TmdbMediaIdDTO(mediaId, mediaType);
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException(e.getMessage());
        }
        return new TmdbMediaIdDTO();
    }


    public List<MediaCarouselResponseDTO> getNowPlayingMedia() {
        try {
            ClassPathResource resource = new ClassPathResource("carousel_data.json");

            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public List<GenreDTO> getGenres() {
        try {
            ClassPathResource resource = new ClassPathResource("genres.json");

            return objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public List<MediaSelectionDTO> getMediaSelection(String query) {
        //https://api.themoviedb.org/3/search/movie
        String movieUrl = "https://api.themoviedb.org/3/search/movie?query=" + query + "&include_adult=true&language=ru-RU&page=1" + "&api_key=" + apiKey;
        String tvUrl = "https://api.themoviedb.org/3/search/tv?query=" + query + "&include_adult=true&language=ru-RU&page=1" + "&api_key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(movieUrl, Map.class);
            Map data = response.getBody();

            if (data == null || !data.containsKey("results") || data.get("results") == null) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");
            if (results == null || results.isEmpty()) {
                return new ArrayList<>();
            }

            List<MediaSelectionDTO> dto = new ArrayList<>();

            for (Map<String, Object> result : results) {
                Number numberMediaId = (Number) result.get("id");
                Long mediaId = numberMediaId != null ? numberMediaId.longValue() : null;
                String releaseDate = result.get("release_date") != null ? result.get("release_date").toString() : "";
                Integer releaseYear = releaseDate.length() >= 4 ? Integer.parseInt(releaseDate.substring(0, 4)) : null;
                dto.add(
                        MediaSelectionDTO
                                .builder()
                                .mediaId(mediaId)
                                .mediaType(MediaType.MOVIE)
                                .title(result.get("title").toString())
                                .releaseYear(releaseYear)
                                .posterUrl(result.get("poster_path") != null ? result.get("poster_path").toString() : null)
                                .build()
                );
            }


            ResponseEntity<Map> secondResponse = restTemplate.getForEntity(tvUrl, Map.class);
            Map secondData = secondResponse.getBody();

            if (secondData == null || !secondData.containsKey("results") || secondData.get("results") == null) {
                return new ArrayList<>();
            }

            List<Map<String, Object>> secondResults = (List<Map<String, Object>>) secondData.get("results");
            if (secondResults == null || secondResults.isEmpty()) {
                return new ArrayList<>();
            }

            for (Map<String, Object> result : secondResults) {
                Number numberMediaId = (Number) result.get("id");
                Long mediaId = numberMediaId != null ? numberMediaId.longValue() : null;
                String firstAirDate = result.get("first_air_date") != null ? result.get("first_air_date").toString() : "";
                Integer releaseYear = firstAirDate.length() >= 4 ? Integer.parseInt(firstAirDate.substring(0, 4)) : null;
                dto.add(
                        MediaSelectionDTO
                                .builder()
                                .mediaId(mediaId)
                                .mediaType(MediaType.TV)
                                .title(result.get("name").toString())
                                .releaseYear(releaseYear)
                                .posterUrl(result.get("poster_path") != null ? result.get("poster_path").toString() : null)
                                .build()
                );
            }


            return dto;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new InvalidResponseFormatException(e.getMessage());
        }

    }


    public List<CountryDTO> getCountries() {
        try {
            ClassPathResource resource = new ClassPathResource("countries.json");

            List<Map<String, Object>> countries = objectMapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<>() {}
            );

            Collections.sort(countries, Comparator.comparing(country -> (String) country.get("country")));

            return countries.stream().map(c -> CountryDTO
                    .builder()
                    .id((Integer) c.get("id"))
                    .country((String) c.get("country"))
                    .build()).toList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }




    // TODO: Write method for searching cast


    //TODO: add another methods
}
