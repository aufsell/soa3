package org.lovesoa.calledadapter.dto;

import jakarta.validation.constraints.*;
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
