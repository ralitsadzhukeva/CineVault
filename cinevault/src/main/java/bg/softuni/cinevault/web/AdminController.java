package bg.softuni.cinevault.web;

import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ModelAndView manageUsers(
            @RequestParam(required = false) String success) {

        ModelAndView mav = new ModelAndView("manage-users");

        mav.addObject("users", userService.findAll());

        if (success != null) {
            mav.addObject("success",
                    "User role updated successfully.");
        }

        return mav;
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable UUID id,
                             @RequestParam Role role) {

        userService.changeRole(id, role);

        return "redirect:/admin/users";
    }
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);

        return "redirect:/admin/users";
    }
}
