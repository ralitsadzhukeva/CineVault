package bg.softuni.cinevault.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
