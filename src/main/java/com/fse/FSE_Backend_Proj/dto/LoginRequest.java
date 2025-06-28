package com.fse.FSE_Backend_Proj.dto;

import com.fse.FSE_Backend_Proj.controller.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    private Role role;
}
