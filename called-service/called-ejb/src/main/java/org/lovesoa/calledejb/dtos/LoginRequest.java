package org.lovesoa.calledejb.dtos;

import javax.validation.constraints.*;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginRequest {

        @NotBlank
        @Email(message = "Некорректная форма email")
        public String email;
        @NotBlank
        public String password;
}