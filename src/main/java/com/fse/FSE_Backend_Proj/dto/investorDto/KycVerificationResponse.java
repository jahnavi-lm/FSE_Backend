package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class KycVerificationResponse {
    private String message;
    private boolean kycStatus;
}
