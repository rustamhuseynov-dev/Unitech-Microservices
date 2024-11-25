package org.example.msuser.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Getter
@Setter
public class UserRequest {

    /*@NotBlank(message = "id is required")
    private Long id;*/

    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}


