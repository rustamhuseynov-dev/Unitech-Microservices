package org.example.msuser.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.*;
import org.example.msuser.Enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Status status;
}

