package bg.softuni.cinevault.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {

    @Size(min = 2, max = 20)
    private String firstName;

    @Size(min = 2, max = 20)
    private String lastName;

    @Email
    private String email;

}
