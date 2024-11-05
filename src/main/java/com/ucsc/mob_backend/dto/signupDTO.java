package com.ucsc.mob_backend.dto;


import com.ucsc.mob_backend.util.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class signupDTO {

    @NotEmpty(message = "Email is required")
    String email;
    @NotEmpty(message = "Password is required")
    String password;

}
