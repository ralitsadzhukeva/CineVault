package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.dto.user.UserEditDto;
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
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        User user = userRepository.findByUsername(userLoginDto.getUsername())
                .orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(
                userLoginDto.getPassword(),
                user.getPassword())) {

            return null;
        }

        return UserLoginDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
    @Override
    public UserEditDto getUserForEdit(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        return UserEditDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
    @Override
    public void update(UUID userId, UserEditDto userEditDto) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setEmail(userEditDto.getEmail());

        userRepository.save(user);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
