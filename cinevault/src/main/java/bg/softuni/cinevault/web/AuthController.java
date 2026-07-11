package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserLoginDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        UserRegisterDto userRegisterDto = UserRegisterDto.builder().build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegisterDto", userRegisterDto);

        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute UserRegisterDto userRegisterDto, BindingResult bindingResult) {

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
    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        UserLoginDto userLoginDto = UserLoginDto.builder().build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("userLoginDto", userLoginDto);

        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute UserLoginDto userLoginDto,
                              BindingResult bindingResult,
                              HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("login");
            return modelAndView;
        }

        UserLoginDto user = userService.login(userLoginDto);

        if (user == null) {
            ModelAndView modelAndView = new ModelAndView("login");

            modelAndView.addObject("userLoginDto", userLoginDto);
            modelAndView.addObject("loginError", "Invalid username or password!");

            return modelAndView;
        }
        httpSession.setAttribute("user_id", user.getId());
        httpSession.setAttribute("user_role", user.getRole());

        return new ModelAndView("redirect:/home");
    }
}
