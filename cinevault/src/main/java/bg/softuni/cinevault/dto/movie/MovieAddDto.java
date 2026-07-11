package bg.softuni.cinevault.dto.movie;

import bg.softuni.cinevault.enums.Genre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MovieAddDto {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Director is required")
    private String director;
    private Genre genre;
    @NotNull(message = "Release year is required")
    @Min(1888)
    @Max(2026)
    private Integer releaseYear;
    private String description;
    private String posterUrl;
}
