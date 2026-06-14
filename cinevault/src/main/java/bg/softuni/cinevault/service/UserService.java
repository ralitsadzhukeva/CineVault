package bg.softuni.cinevault.service;

import bg.softuni.cinevault.dto.user.UserLoginDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserRegisterDto register(UserRegisterDto userRegisterDto);
    UserLoginDto login(UserLoginDto userLoginDto);
    List<User> findAll();

    User findById(UUID userId);
}
