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
public class RegisterRequest{
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email;
        @NotBlank(message = "Password cannot be blank")
        String password;
        @NotBlank(message = "Name cannot be blank")
        String name;
}