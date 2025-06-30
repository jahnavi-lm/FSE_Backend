package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InvestorKycStatusDto {
    private String investorId;
    private boolean kycStatus;
}
