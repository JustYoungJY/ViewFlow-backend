package app.viewflowbackend.DTO.api;


import app.viewflowbackend.enums.MediaType;
import app.viewflowbackend.enums.RandomType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RandomMediaCardRequestDTO {

    private GenreDTO genre;

    @Min(value = 1900, message = "Год должен быть больше или равен 1900")
    @NotNull(message = "Год не может быть пустым")
    private Integer minYear;

    @Max(value = 2025, message = "Год должен быть меньше или равен 2025")
    @NotNull(message = "Год не может быть пустым")
    private Integer maxYear;

    @Min(value = 0, message = "Рейтинг должен быть больше или равен 0")
    @NotNull(message = "Рейтинг не может быть пустым")
    private Double minRating;

    @Max(value = 10, message = "Рейтинг должен быть меньше или равен 10")
    @NotNull(message = "Рейтинг не может быть пустым")
    private Double maxRating;

    @NotNull(message = "Тип не может быть пустым")
    private RandomType randomType;

    public RandomMediaCardRequestDTO(Integer minYear, Integer maxYear, Double minRating, Double maxRating, RandomType randomType) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.randomType = randomType;
    }
}
