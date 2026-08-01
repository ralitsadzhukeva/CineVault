package bg.softuni.cinevault.scheduler;


import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.service.recommendation.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RecommendationScheduler {
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    public RecommendationScheduler(RecommendationService recommendationService, UserRepository userRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void regenerateRecommendations() {
        log.info("Starting scheduled recommendation generation.");

        List<User> users = userRepository.findAll();

        for (User user : users) {

            try {
                    recommendationService.generateRecommendations(user.getId());

                    log.info("Recommendations generated for user {}.", user.getUsername());
            }
            catch (Exception e) {
                log.error("Failed to generate recommendations for user {}.", user.getUsername(), e);
            }
        }
        log.info("Scheduled recommendation generation completed.");
    }
}
