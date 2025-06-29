package com.fse.FSE_Backend_Proj.dto.authDto;

import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;
}