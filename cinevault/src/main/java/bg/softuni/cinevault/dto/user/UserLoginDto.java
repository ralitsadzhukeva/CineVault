package bg.softuni.cinevault.dto.user;

import bg.softuni.cinevault.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserLoginDto {
    private UUID id;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    private Role role;
}
