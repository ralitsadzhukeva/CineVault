package bg.softuni.cinevault.web;

import bg.softuni.cinevault.enums.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @GetMapping("/home")
    public ModelAndView home(HttpSession session) {
        ModelAndView mav = new ModelAndView("home");

        if (session.getAttribute("user_id") == null) {
            return new ModelAndView("redirect:/login");
        }
        mav.addObject("isAdmin",
                session.getAttribute("user_role") == Role.ADMIN);

        return mav;
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
