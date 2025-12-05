package app.viewflowbackend.DTO.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TorrentOptionDTO {

    private String title;

    private String size;

    private int seeds;

    private String link;

    private String resolution;

    private String quality;
}