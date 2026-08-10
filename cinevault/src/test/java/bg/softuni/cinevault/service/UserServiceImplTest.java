package bg.softuni.cinevault.service;

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
import bg.softuni.cinevault.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private WatchlistRepository watchlistRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("lanita")
                .firstName("Lana")
                .lastName("Del Rey")
                .email("lanita@gmail.com")
                .password("123456")
                .role(Role.USER)
                .createdOn(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    void register_shouldCreateUser() {

        UserRegisterDto dto = UserRegisterDto.builder()
                .username("miLana")
                .email("milana@abv.bg")
                .password("pokemon")
                .confirmPassword("pokemon")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(dto.getUsername()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(dto.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserRegisterDto result = userService.register(dto);

        assertNotNull(result);
        assertEquals(dto, result);

        verify(userRepository).findByUsername(dto.getUsername());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Transactional
    void register_shouldThrowException_whenUsernameIsTaken() {
        UserRegisterDto dto = UserRegisterDto.builder()
                .username("miLana")
                .email("milana@abv.bg")
                .password("pokemon")
                .confirmPassword("pokemon")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(dto.getUsername()))
                .thenReturn(Optional.of(user));

        assertThrows(DuplicateUsernameException.class, () -> userService.register(dto));

        verify(userRepository).findByUsername(dto.getUsername());
    }

    @Test
    void getUserForEdit_shouldReturnUser(){
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        var result = userService.getUserForEdit(user.getId());
        assertNotNull(result);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void getUserForEdit_shouldThrowException_whenUserDoesNotExist(){
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserForEdit(user.getId()));

        verify(userRepository).findById(user.getId());
    }

    @Test
    @Transactional
    void updateUser_shouldReturnUpdatedUser(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        UserEditDto userEditDto = UserEditDto.builder()
                    .firstName("Elizabeth")
                    .lastName("Grant")
                    .email("lizziegrant@gmail.com")
                    .build();
        userService.update(userId,userEditDto);

        assertEquals("Elizabeth",user.getFirstName());
        assertEquals("Grant",user.getLastName());
        assertEquals("lizziegrant@gmail.com",user.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userId,UserEditDto.builder().build()));

        verify(userRepository).findById(userId);
    }

    @Test
    @Transactional
    void changeRole_shouldChangeRole(){
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        userService.changeRole(userId, Role.ADMIN);

        assertEquals(Role.ADMIN, user.getRole());

        verify(userRepository).findById(userId);
        verify(userRepository).save(user);

        SecurityContextHolder.clearContext();
    }

    @Test
    void changeRole_shouldThrowException_whenUserDoesNotExist(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.changeRole(userId, Role.ADMIN));
        verify(userRepository).findById(userId);
    }
    @Test
    void changeRole_shouldThrowException_whenUserDoesNotHavePermission() {
        UUID userId = user.getId();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        assertThrows(
                AccessDeniedException.class,
                () -> userService.changeRole(userId, Role.USER)
        );

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));

        SecurityContextHolder.clearContext();
    }

    @Test
    @Transactional
    void deleteUser_shouldDeleteUser(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);
        verify(reviewRepository).deleteByUserId(userId);
        verify(watchlistRepository).deleteByUserId(userId);
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserDoesNotExist(){
        UUID userId = user.getId();
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_shouldReturnUser(){
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        var result = userService.findById(user.getId());
        assertNotNull(result);
        verify(userRepository).findById(user.getId());
    }

    @Test
    void findById_shouldThrowException_whenUserDoesNotExist(){
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void findAll_shouldReturnAllUsers(){
        when(userRepository.findAll())
                .thenReturn(List.of(user));
        var result = userService.findAll();
        assertNotNull(result);
        verify(userRepository).findAll();
    }
}