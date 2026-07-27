package bg.softuni.cinevault.service.recommendation;

import bg.softuni.cinevault.dto.recommendation.RecommendationDto;
import bg.softuni.cinevault.dto.recommendation.RecommendationViewDto;

import java.util.List;
import java.util.UUID;

public interface RecommendationService {
    void generateRecommendations(UUID userId);

    List<RecommendationViewDto> getRecommendations(UUID userId);

    void deleteRecommendations(UUID userId);
}
