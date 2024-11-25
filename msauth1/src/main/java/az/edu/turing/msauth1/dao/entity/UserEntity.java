package az.edu.turing.msauth1.dao.entity;

import az.edu.turing.msauth1.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    public UserEntity(String fullName, String username, UserRole role) {
        this.fullName = fullName;
        this.username = username;
        this.role = role;
    }
}
