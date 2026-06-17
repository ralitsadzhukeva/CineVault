package bg.softuni.cinevault.web;

import bg.softuni.cinevault.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review/add/{movieId}")
    public String addReview(@PathVariable UUID movieId, @RequestParam Integer rating,
                            @RequestParam String comment,HttpSession session){

        UUID userId = (UUID) session.getAttribute("user_id");

        if (userId == null) {
            return "redirect:/login";
        }

        reviewService.addReview(movieId, userId, rating, comment);

        return "redirect:/reviews" ;
    }
    @GetMapping("/reviews")
    public ModelAndView userReviews(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");

        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("reviews");

        mav.addObject("reviews",
                reviewService.getUserReviews(userId));

        return mav;
    }
}
