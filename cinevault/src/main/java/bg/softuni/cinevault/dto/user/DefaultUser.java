package bg.softuni.cinevault.dto.user;

import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultUser implements CommandLineRunner {

    private final UserService userService;

    public DefaultUser(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.createDefaultAdmin();
    }
}