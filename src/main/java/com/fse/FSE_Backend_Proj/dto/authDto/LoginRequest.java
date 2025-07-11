package com.fse.FSE_Backend_Proj.dto.authDto;

import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String email;
    private String password;
    private UserRole role;
}
