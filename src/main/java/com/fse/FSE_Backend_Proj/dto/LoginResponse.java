package com.fse.FSE_Backend_Proj.dto;

import com.fse.FSE_Backend_Proj.entites.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private User user;
}
