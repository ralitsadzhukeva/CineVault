package bg.softuni.cinevault.dto.recommendation;

import bg.softuni.cinevault.enums.Genre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MoviePreferenceDto {
    @NotNull
    private UUID movieId;
    @NotNull
    private Genre genre;
    @NotNull
    @Min(1)
    @Max(10)
    private Integer rating;
    private boolean watched;
}
