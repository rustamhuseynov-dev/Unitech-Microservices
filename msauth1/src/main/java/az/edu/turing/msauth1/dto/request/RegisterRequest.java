package az.edu.turing.msauth1.dto.request;

import az.edu.turing.msauth1.model.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String fullname;
    private String email;
    private String username;
    private String password;
    private UserRole role;

}
