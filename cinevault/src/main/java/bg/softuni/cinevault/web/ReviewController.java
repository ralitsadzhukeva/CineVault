package bg.softuni.cinevault.web;

import bg.softuni.cinevault.entities.Review;
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

        return "redirect:/movies/" + movieId;
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
    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable UUID id, HttpSession session) {
        if (session.getAttribute("user_id") == null) {
            return "redirect:/login";
        }
        reviewService.deleteReview(id);

        return "redirect:/reviews";
    }
    @GetMapping("/reviews/edit/{id}")
    public ModelAndView editReview(@PathVariable UUID id, HttpSession session) {
        ModelAndView mav = new ModelAndView("review-edit");

        Review review = reviewService.findById(id);
        mav.addObject("review", review);

        return mav;
    }
    @PostMapping("/reviews/edit/{id}")
    public String editReviewConfirm(@PathVariable UUID id, @RequestParam Integer rating, @RequestParam String comment, HttpSession session) {
        reviewService.editReview(id, rating, comment);

        return "redirect:/reviews";
    }
}
