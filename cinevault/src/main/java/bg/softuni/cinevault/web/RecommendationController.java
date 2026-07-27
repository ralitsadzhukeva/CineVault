package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.recommendation.RecommendationDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.recommendation.RecommendationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public ModelAndView recommendations(@AuthenticationPrincipal User user) {

        ModelAndView mav = new ModelAndView("recommendations");

        mav.addObject(
                "recommendations",
                recommendationService.getRecommendations(user.getId()));

        return mav;
    }

    @PostMapping("/recommendations/generate")
    public String generateRecommendations(@AuthenticationPrincipal User user) {

        recommendationService.generateRecommendations(user.getId());

        return "redirect:/recommendations";
    }

    @PostMapping("/recommendations/delete")
    public String deleteRecommendations(@AuthenticationPrincipal User user) {

        recommendationService.deleteRecommendations(user.getId());

        return "redirect:/recommendations";
    }
}