package bg.softuni.cinevault.web;

import bg.softuni.cinevault.dto.user.UserRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        UserRegisterDto userRegisterDto = UserRegisterDto.builder().build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("userRegisterDto", userRegisterDto);

        return modelAndView;
    }
}
