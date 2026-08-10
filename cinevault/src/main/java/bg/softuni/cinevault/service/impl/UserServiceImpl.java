package bg.softuni.cinevault.service.impl;

import bg.softuni.cinevault.dto.user.UserEditDto;
import bg.softuni.cinevault.dto.user.UserRegisterDto;
import bg.softuni.cinevault.entities.User;
import bg.softuni.cinevault.enums.Role;
import bg.softuni.cinevault.exception.AccessDeniedException;
import bg.softuni.cinevault.exception.user.DuplicateUsernameException;
import bg.softuni.cinevault.exception.user.UserNotFoundException;
import bg.softuni.cinevault.repository.ReviewRepository;
import bg.softuni.cinevault.repository.UserRepository;
import bg.softuni.cinevault.repository.WatchlistRepository;
import bg.softuni.cinevault.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final WatchlistRepository watchlistRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ReviewRepository reviewRepository, WatchlistRepository watchlistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reviewRepository = reviewRepository;
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public UserRegisterDto register(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUsername(userRegisterDto.getUsername()).isPresent()) {

            log.warn("Registration failed. Username already exists: {}",
                    userRegisterDto.getUsername());

            throw new DuplicateUsernameException();
        }

        User user = User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("User registered successfully. Username: {}",
                userRegisterDto.getUsername());

        return userRegisterDto;
    }
    @Override
    public UserEditDto getUserForEdit(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        return UserEditDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
    @Override
    public void update(UUID userId, UserEditDto userEditDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));

        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setEmail(userEditDto.getEmail());

        log.info("User profile updated. Username: {}, ID: {}",
                user.getUsername(),
                userId);

        userRepository.save(user);
    }

    @Override
    public void changeRole(UUID userId, Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Role oldRole = user.getRole();

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(user.getId())
                && role == Role.USER) {

            throw new AccessDeniedException();
        }

        user.setRole(role);

        log.info("User role changed. Username: {}, Old role: {}, New role: {}",
                user.getUsername(),
                oldRole,
                role);

        userRepository.save(user);
    }
    @Override
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        log.info("User deleted. Username: {}, ID: {}",
                user.getUsername(),
                id);

        reviewRepository.deleteByUserId(id);
        watchlistRepository.deleteByUserId(id);

        userRepository.delete(user);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }
    @Override
    public void createDefaultAdmin() {

        if (!userRepository.findByUsername("admin").isPresent()) {

            User admin = User.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("adminPass"))
                    .role(Role.ADMIN)
                    .createdOn(LocalDateTime.now())
                    .build();

            userRepository.save(admin);

            log.info("Default admin user created with username [{}].",
                    admin.getUsername());
        }
    }

}
