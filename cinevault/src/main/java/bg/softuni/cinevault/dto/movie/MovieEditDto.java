package bg.softuni.cinevault.dto.movie;


import bg.softuni.cinevault.enums.Genre;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MovieEditDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String title;

    @NotBlank
    private String director;

    @NotNull
    private Genre genre;

    @Min(1888)
    @Max(2026)
    private Integer releaseYear;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    private String posterUrl;
}
