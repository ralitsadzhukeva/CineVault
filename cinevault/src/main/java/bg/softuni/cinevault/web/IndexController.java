package bg.softuni.cinevault.web;

import bg.softuni.cinevault.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class IndexController {
    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
