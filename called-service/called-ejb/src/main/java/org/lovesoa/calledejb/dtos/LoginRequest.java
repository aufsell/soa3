package org.lovesoa.calledejb.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

        @NotBlank
        @Email(message = "Некорректная форма email")
        public String email;
        @NotBlank
        public String password;
}