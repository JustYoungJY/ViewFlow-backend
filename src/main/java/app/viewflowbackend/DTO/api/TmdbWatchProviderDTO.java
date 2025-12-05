package app.viewflowbackend.DTO.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TmdbWatchProviderDTO {
    private String kinopoisk;
    private String okko;
    private String googlePlay;
    private String appleTv;
    private String netflix;
    private String hbo;

    public boolean isAllFilled() {
        return kinopoisk != null && okko != null && googlePlay != null
                && appleTv != null && netflix != null && hbo != null;
    }

    public boolean isEmpty() {
        return kinopoisk == null && okko == null && googlePlay == null
                && appleTv == null && netflix == null && hbo == null;
    }
}