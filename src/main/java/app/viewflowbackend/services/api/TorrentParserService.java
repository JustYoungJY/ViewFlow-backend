package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.TorrentOptionDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TorrentParserService {

    private static final String BASE_URL = "https://searchtor.to";
    private static final String SEARCH_PATH = "/r/";


    public List<TorrentOptionDTO> getMovieTorrents(String movieTitle) {
        String queryUrl = buildSearchUrl(movieTitle);
        return parseAndFilter(queryUrl);
    }


    public List<TorrentOptionDTO> getSeriesSeasonTorrents(String seriesTitle, int seasonNumber) {
        String query = seriesTitle + " " + seasonNumber + " сезон";
        String hyphenatedQuery = query.trim().replace(" ", "-");
        String queryUrl = buildSearchUrl(hyphenatedQuery);
        return parseAndFilter(queryUrl);
    }


    private String buildSearchUrl(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        return BASE_URL + SEARCH_PATH + encodedQuery;
    }

    private List<TorrentOptionDTO> parseAndFilter(String url) {
        List<TorrentOptionDTO> results = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            Elements rows = doc.select("table.pline tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                if (cols.size() < 6) continue;

                Element titleElement = cols.get(0).selectFirst("a");
                String rawTitle = titleElement != null ? titleElement.text() : "";

                String size = cols.get(1).text();

                int seeds = 0;
                try {
                    seeds = Integer.parseInt(cols.get(2).text());
                } catch (NumberFormatException ignored) {
                }


                Element magnetElement = cols.get(5).selectFirst("a");
                String relativeLink = magnetElement != null ? magnetElement.attr("href") : "";
                String absoluteLink = BASE_URL + relativeLink;

                if (isValidQuality(rawTitle)) {
                    String resolution = extractResolution(rawTitle);
                    String quality = extractQuality(rawTitle);

                    TorrentOptionDTO dto = TorrentOptionDTO.builder()
                            .title(rawTitle)
                            .size(size)
                            .seeds(seeds)
                            .link(absoluteLink)
                            .resolution(resolution)
                            .quality(quality)
                            .build();

                    results.add(dto);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return results.stream()
                .sorted(Comparator.comparingInt(TorrentOptionDTO::getSeeds).reversed())
                .collect(Collectors.toList());
    }


    private boolean isValidQuality(String title) {
        String lower = title.toLowerCase();
        return lower.contains("bdrip") || lower.contains("hdrip") || lower.contains("web-dl") || lower.contains("webrip");
    }

    private String extractQuality(String title) {
        String lower = title.toLowerCase();
        if (lower.contains("bdrip")) return "BDRip";
        if (lower.contains("hdrip")) return "HDRip";
        if (lower.contains("web-dl")) return "WEB-DL";
        if (lower.contains("webrip")) return "WEBRip";
        return "Unknown";
    }

    private String extractResolution(String title) {
        String lower = title.toLowerCase();

        if (lower.contains("2160") || lower.contains("4k") || lower.contains("uhd")) {
            return "4K";
        }

        if (lower.contains("1080")) {
            return "1080p";
        }

        if (lower.contains("720")) {
            return "720p";
        }

        return "SD";
    }
}