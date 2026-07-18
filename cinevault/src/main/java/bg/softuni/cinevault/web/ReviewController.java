package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.review.ReviewAddDto;
import bg.softuni.cinevault.entities.Review;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.security.access.AccessDeniedException;
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
                                  @AuthenticationPrincipal User user) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("movie-details");

            return mav;
        }

        reviewService.addReview(movieId, user.getId(), reviewAddDto.getRating(), reviewAddDto.getComment());

        return new ModelAndView("redirect:/movies/" + movieId);
    }

    @GetMapping("/reviews")
    public ModelAndView userReviews(@AuthenticationPrincipal User user) {


        ModelAndView mav = new ModelAndView("reviews");

        mav.addObject("reviews",
                reviewService.getUserReviews(user.getId()));

        return mav;
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable UUID id, @AuthenticationPrincipal User user){

        reviewService.deleteReview(id,user);

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
                                    BindingResult bindingResult,
                                          @AuthenticationPrincipal User currentUser) throws AccessDeniedException {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("review-edit");
            mav.addObject("review", reviewService.findById(id));
            return mav;
        }
        reviewService.editReview(id,currentUser ,reviewAddDto.getRating(), reviewAddDto.getComment());

        return new ModelAndView("redirect:/reviews");
    }
}
