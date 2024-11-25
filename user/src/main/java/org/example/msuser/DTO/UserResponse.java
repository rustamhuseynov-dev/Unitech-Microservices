package org.example.msuser.DTO;

import lombok.*;
import org.example.msuser.Enums.Status;


@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@Getter
@Setter
public class UserResponse {

    private Long id;
    private String username;

    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
}