package app.viewflowbackend.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kinobox")
public class KinoboxController {

    private static final String KINOBOX_PLAYER_TEMPLATE =
            "<div class=\"kinobox\" data-kinopoisk=\"%d\" data-title=\"\" data-menu=\"list\"></div>";

    @GetMapping(value = "/player", produces = "text/html")
    public String getKinoboxPlayer(@RequestParam Integer kinopoiskId) {

        if (kinopoiskId == null || kinopoiskId <= 0) {
            return "";
        }

        return String.format(KINOBOX_PLAYER_TEMPLATE, kinopoiskId);
    }
}
