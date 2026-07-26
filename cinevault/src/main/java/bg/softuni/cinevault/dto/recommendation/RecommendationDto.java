package bg.softuni.cinevault.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
    private UUID movieId;
    private String reason;
    private Integer score;

}
