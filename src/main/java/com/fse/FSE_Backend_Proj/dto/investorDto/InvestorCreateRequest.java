package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvestorCreateRequest {
    private String userId;  // FK from User table
    private String panNumber;
    private String address;
    private String guardianName;
    private String occupation;
    private BigDecimal annualIncome;
    private String nomineeName;
    private String bankAccountNo;
    private String ifscCode;
    private LocalDate dob;
    private String kycDocUrl; // optional
}
