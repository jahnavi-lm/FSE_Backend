package com.fse.FSE_Backend_Proj.dto.authDto;

import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private UserRole role;
}
