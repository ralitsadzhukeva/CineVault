package bg.softuni.cinevault.dto.recommendation;

import bg.softuni.cinevault.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationViewDto {

    private UUID movieId;

    private String title;

    private String posterUrl;

    private Genre genre;

    private Double averageRating;

    private String reason;

    private Integer score;
}