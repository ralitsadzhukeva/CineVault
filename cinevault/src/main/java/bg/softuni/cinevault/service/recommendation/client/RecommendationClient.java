package bg.softuni.cinevault.service.recommendation.client;

import bg.softuni.cinevault.dto.recommendation.RecommendationDto;
import bg.softuni.cinevault.dto.recommendation.RecommendationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "recommendation-service",
        url = "http://localhost:8081")
public interface RecommendationClient {
    @GetMapping("/api/recommendations/{userId}")
    List<RecommendationDto> getRecommendations(
            @PathVariable UUID userId);

    @PostMapping("/api/recommendations/generate")
    void generateRecommendations(
            @RequestBody RecommendationRequestDto request);

    @PutMapping("/api/recommendations/regenerate")
    void regenerateRecommendations(
            @RequestBody RecommendationRequestDto request);

    @DeleteMapping("/api/recommendations/{userId}")
    void deleteRecommendations(
            @PathVariable UUID userId);
}
