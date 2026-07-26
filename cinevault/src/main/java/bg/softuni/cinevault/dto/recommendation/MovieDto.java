package bg.softuni.cinevault.dto.recommendation;

import bg.softuni.cinevault.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {

    private UUID movieId;

    private Genre genre;

    private Double averageRating;
}
