package app.viewflowbackend.services.api;

import app.viewflowbackend.DTO.api.ZonaMovieDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ZonaParserService {

    private static final String BASE_URL = "https://w140.zona.plus";
    private static final String SEARCH_PATH = "/search/";

    public ZonaMovieDTO findMovie(String title, Integer targetYear) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String searchUrl = BASE_URL + SEARCH_PATH + encodedTitle;


            Document doc = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();


            Elements items = doc.select("li.results-item-wrap");

            for (Element item : items) {
                String yearText = item.select(".results-item-year").text().trim();

                String titleText = item.select(".results-item-title").text().trim();

                try {
                    Integer year = Integer.parseInt(yearText);

                    if (year.equals(targetYear) && titleText.toLowerCase().contains(title.toLowerCase())) {

                        Element linkElement = item.selectFirst("a.results-item");
                        if (linkElement != null) {
                            String relativeUrl = linkElement.attr("href");
                            String fullUrl = BASE_URL + relativeUrl;

                            return new ZonaMovieDTO(titleText, year, fullUrl);
                        }
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}