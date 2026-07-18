package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserLoginDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute UserRegisterDto userRegisterDto,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("register");
            mav.addObject("userRegisterDto", userRegisterDto);
            return mav;
        }

        try {
            userService.register(userRegisterDto);

            return new ModelAndView("redirect:/login");

        } catch (IllegalArgumentException e) {

            ModelAndView mav = new ModelAndView("register");

            mav.addObject("userRegisterDto", userRegisterDto);
            mav.addObject("errorMessage", e.getMessage());

            return mav;
        }
    }
    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        UserRegisterDto userRegisterDto = UserRegisterDto.builder().build();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegisterDto", userRegisterDto);
        return modelAndView;
    }
    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        UserLoginDto userLoginDto = UserLoginDto.builder().build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginDto", userLoginDto);

        return modelAndView;
    }
}
