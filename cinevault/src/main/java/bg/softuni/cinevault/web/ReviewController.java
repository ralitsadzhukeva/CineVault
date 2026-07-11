package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.review.ReviewAddDto;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review/add/{movieId}")
    public ModelAndView addReview(@PathVariable UUID movieId,
                            @Valid @ModelAttribute ReviewAddDto reviewAddDto,
                            BindingResult bindingResult,
                            HttpSession session) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("movie-details");

            return mav;
        }
        UUID userId = (UUID) session.getAttribute("user_id");

        reviewService.addReview(movieId, userId, reviewAddDto.getRating(), reviewAddDto.getComment());

        return new ModelAndView("redirect:/movies/" + movieId);
    }

    @GetMapping("/reviews")
    public ModelAndView userReviews(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");


        ModelAndView mav = new ModelAndView("reviews");

        mav.addObject("reviews",
                reviewService.getUserReviews(userId));

        return mav;
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable UUID id) {

        reviewService.deleteReview(id);

        return "redirect:/reviews";
    }

    @GetMapping("/reviews/edit/{id}")
    public ModelAndView editReview(@PathVariable UUID id) {
        ModelAndView mav = new ModelAndView("review-edit");

        Review review = reviewService.findById(id);
        mav.addObject("review", review);

        return mav;
    }

    @PostMapping("/reviews/edit/{id}")
    public ModelAndView editReviewConfirm(@PathVariable UUID id,
                                    @Valid @ModelAttribute ReviewAddDto reviewAddDto,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("review-edit");
            mav.addObject("review", reviewService.findById(id));
            return mav;
        }
        reviewService.editReview(id, reviewAddDto.getRating(), reviewAddDto.getComment());

        return new ModelAndView("redirect:/reviews");
    }
}
