package az.edu.turing.msauth1.model;

import az.edu.turing.msauth1.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String fullName;
    private String username;
    private String password;
    private UserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User userDto = (User) o;
        return password.equals(userDto.password) && username.equals(userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
