package com.fse.FSE_Backend_Proj.dto.investorDto;


import lombok.Data;

@Data
public class KycVerificationRequest {
    private String investorId;
    private String kycDocUrl; // or MultipartFile if using file upload later
}
