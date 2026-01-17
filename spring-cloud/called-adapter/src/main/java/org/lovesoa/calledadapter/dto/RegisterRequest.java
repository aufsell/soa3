package org.lovesoa.calledadapter.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest{
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email;
        @NotBlank(message = "Password cannot be blank")
        String password;
        @NotBlank(message = "Name cannot be blank")
        String name;
}
