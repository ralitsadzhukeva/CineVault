package bg.softuni.cinevault.service;

import bg.softuni.cinevault.dto.user.UserEditDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserRegisterDto register(UserRegisterDto userRegisterDto);
    List<User> findAll();
    User findById(UUID userId);
    UserEditDto getUserForEdit(UUID userId);
    void update(UUID userId, UserEditDto userEditDto);
    void changeRole(UUID userId, Role role);
    void deleteUser(UUID id);
    void createDefaultAdmin();
}
