package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserEditDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.service.UserService;
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

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");

        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        User user = userService.findById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("profile");

        modelAndView.addObject("user", user);

        return modelAndView;
    }
    @GetMapping("/profile/edit")
    public ModelAndView editProfile(HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");

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