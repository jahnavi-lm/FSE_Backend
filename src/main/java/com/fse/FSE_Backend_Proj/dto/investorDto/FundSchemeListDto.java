package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FundSchemeListDto {
    private String id;
    private String fundName;
    private BigDecimal nav;
    private String category;
    private String riskLevel;
    private BigDecimal amount; // AUM
}
