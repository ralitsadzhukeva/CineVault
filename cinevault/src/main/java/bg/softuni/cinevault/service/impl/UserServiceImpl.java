package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.dto.user.UserLoginDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegisterDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUsername(userRegisterDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return userRegisterDto;
    }

    @Override
    public UserLoginDto login(UserLoginDto userLoginDto) {
        return null;
    }

}
