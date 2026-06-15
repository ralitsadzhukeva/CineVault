package bg.softuni.cinevault.dto.user;

import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DefaultUser implements CommandLineRunner {
    private final UserService userService;

    public DefaultUser(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {
        List<User> users = userService.findAll();
        if (!users.isEmpty()) {
            return;
        }
        UserRegisterDto userRegisterDto = UserRegisterDto.builder()
                .username("admin")
                .email("admin@admin.com")
                .password("adminPass")
                .confirmPassword("adminPass")
                .role(Role.ADMIN)
                .build();

        userService.register(userRegisterDto);

        log.info("Default user created with username [%s] and password [%s].".formatted(
                userRegisterDto.getUsername(), userRegisterDto.getPassword()));
    }
}
