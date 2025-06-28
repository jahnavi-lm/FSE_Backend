package com.fse.FSE_Backend_Proj.dto.authDto;

import com.fse.FSE_Backend_Proj.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
//    private String refreshToken;
    private User user;
}
