package com.fse.FSE_Backend_Proj.dto.authDto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
}