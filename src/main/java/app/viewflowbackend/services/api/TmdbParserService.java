package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.TmdbWatchProviderDTO;
import app.viewflowbackend.enums.MediaType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TmdbParserService {

    private static final String BASE_URL = "https://www.themoviedb.org";
    // https://www.themoviedb.org/movie/123/watch?locale=RU
    private static final String WATCH_URL_TEMPLATE = "%s/%s/%d/watch?locale=%s";

    public TmdbWatchProviderDTO getWatchProviders(Long mediaId, MediaType mediaType) {
        TmdbWatchProviderDTO dto = new TmdbWatchProviderDTO();

        fetchAndParse(mediaId, mediaType, "RU", dto);

        if (!dto.isAllFilled()) {
            fetchAndParse(mediaId, mediaType, "US", dto);
        }

        if (dto.isEmpty()) {
            return null;
        }

        return dto;
    }

    private void fetchAndParse(Long mediaId, MediaType mediaType, String locale, TmdbWatchProviderDTO dto) {
        String typeStr = mediaType.name().toLowerCase();
        String url = String.format(WATCH_URL_TEMPLATE, BASE_URL, typeStr, mediaId, locale);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements links = doc.select("ul.providers li a[title]");

            for (Element link : links) {
                String titleAttr = link.attr("title").toLowerCase();
                String href = link.attr("href");

                fillDto(dto, titleAttr, href);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при парсинге TMDB (" + locale + "): " + e.getMessage());
        }
    }

    private void fillDto(TmdbWatchProviderDTO dto, String titleLower, String url) {

        if (dto.getKinopoisk() == null && (titleLower.contains("kinopoisk") || titleLower.contains("кинопоиск"))) {
            dto.setKinopoisk(url);
        } else if (dto.getOkko() == null && (titleLower.contains("okko") || titleLower.contains("окко"))) {
            dto.setOkko(url);
        } else if (dto.getGooglePlay() == null && titleLower.contains("google play")) {
            dto.setGooglePlay(url);
        } else if (dto.getAppleTv() == null && titleLower.contains("apple tv")) {
            dto.setAppleTv(url);
        } else if (dto.getNetflix() == null && titleLower.contains("netflix")) {
            dto.setNetflix(url);
        } else if (dto.getHbo() == null && (titleLower.contains("hbo"))) {
            dto.setHbo(url);
        }
    }
}