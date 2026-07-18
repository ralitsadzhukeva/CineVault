package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserEditDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.UserService;
import bg.softuni.cinevault.service.WatchlistService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ProfileController {
    private final UserService userService;
    private final WatchlistService watchlistService;
    private final ReviewService reviewService;

    public ProfileController(UserService userService, WatchlistService watchlistService, ReviewService reviewService) {
        this.userService = userService;
        this.watchlistService = watchlistService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public ModelAndView profile(@AuthenticationPrincipal User user) {


        ModelAndView mav = new ModelAndView("profile");

        mav.addObject("user", userService.findById(user.getId()));

        mav.addObject("watchlistCount",
                watchlistService.getUserWatchlist(user.getId()).size());

        mav.addObject("reviewCount",
                reviewService.getUserReviews(user.getId()).size());

        return mav;
    }
    @GetMapping("/profile/edit")
    public ModelAndView editProfile(@AuthenticationPrincipal User user) {

        UserEditDto userEditDto =
                userService.getUserForEdit(user.getId());

        ModelAndView mav =
                new ModelAndView("profile-edit");

        mav.addObject("userEditDto", userEditDto);

        return mav;
    }
    @PostMapping("/profile/edit")
    public ModelAndView editProfile(
            @Valid @ModelAttribute UserEditDto userEditDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal User user) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("profile-edit");
            mav.addObject("userEditDto", userEditDto);
            return mav;
        }

        userService.update(user.getId(), userEditDto);

        return new ModelAndView("redirect:/profile");
    }
}