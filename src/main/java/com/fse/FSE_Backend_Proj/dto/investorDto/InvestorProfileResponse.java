package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private String panNumber;
    private String address;
    private String guardianName;
    private String occupation;
    private BigDecimal annualIncome;
    private String nomineeName;
    private String bankAccountNo;
    private String ifscCode;
    private LocalDate dob;
    private String kycDocUrl;
}
