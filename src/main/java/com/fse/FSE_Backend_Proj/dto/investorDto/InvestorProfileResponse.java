package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class InvestorProfileResponse {
    private String id;
    private String name;
    private String email;
    private boolean kycStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
