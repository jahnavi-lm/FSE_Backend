package com.fse.FSE_Backend_Proj.dto;

import com.fse.FSE_Backend_Proj.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
