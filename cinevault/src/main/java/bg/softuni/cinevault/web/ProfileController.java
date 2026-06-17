package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserEditDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.ReviewService;
import bg.softuni.cinevault.service.UserService;
import bg.softuni.cinevault.service.WatchlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

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
    public ModelAndView profile(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");

        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mav = new ModelAndView("profile");

        mav.addObject("user", userService.findById(userId));

        mav.addObject("watchlistCount",
                watchlistService.getUserWatchlist(userId).size());

        mav.addObject("reviewCount",
                reviewService.getUserReviews(userId).size());

        return mav;
    }
    @GetMapping("/profile/edit")
    public ModelAndView editProfile(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        UserEditDto userEditDto =
                userService.getUserForEdit(userId);

        ModelAndView mav =
                new ModelAndView("profile-edit");

        mav.addObject("userEditDto", userEditDto);

        return mav;
    }
    @PostMapping("/profile/edit")
    public ModelAndView editProfile(
            @ModelAttribute UserEditDto userEditDto,
            HttpSession session) {

        UUID userId =
                (UUID) session.getAttribute("user_id");

        userService.update(userId, userEditDto);

        return new ModelAndView("redirect:/profile");
    }
}