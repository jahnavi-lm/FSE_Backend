package com.fse.FSE_Backend_Proj.dto.investorDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycVerificationRequest {
    private String investorId;
    private String kycDocUrl; // or MultipartFile if using file upload later
}
