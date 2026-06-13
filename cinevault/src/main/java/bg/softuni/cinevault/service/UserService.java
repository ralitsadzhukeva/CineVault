package bg.softuni.cinevault.service;

import bg.softuni.cinevault.dto.user.UserLoginDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;

public interface UserService {
    UserRegisterDto register(UserRegisterDto userRegisterDto);
    UserLoginDto login(UserLoginDto userLoginDto);
}
