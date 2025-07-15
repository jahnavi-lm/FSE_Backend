package com.fse.FSE_Backend_Proj.dto.authDto;

import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
}
