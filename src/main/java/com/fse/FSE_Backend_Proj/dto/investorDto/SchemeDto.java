package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchemeDto {
    private String schemeId; // change from Long to String
    private String name;
    private String type;
    private String riskLevel;
    private double amount;
    private String objective;
}