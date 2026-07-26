package bg.softuni.cinevault.service.recommendation;

import java.util.UUID;

public interface RecommendationService {
    void generateRecommendations(UUID userId);
}
