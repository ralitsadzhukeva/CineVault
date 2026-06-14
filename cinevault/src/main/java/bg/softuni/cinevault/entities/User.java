package bg.softuni.cinevault.entities;

import bg.softuni.cinevault.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(unique=true,nullable=false)
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique=true,nullable=false)
    @Email
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String confirmPassword;
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable=false)
    private LocalDateTime createdOn;
}
