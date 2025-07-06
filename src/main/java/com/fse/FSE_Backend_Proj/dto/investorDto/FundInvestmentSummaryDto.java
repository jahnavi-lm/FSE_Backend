package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FundInvestmentSummaryDto {
    private String fundName;
    private BigDecimal avgNav;           // ✅ Add this line
    private BigDecimal investedAmount;
    private BigDecimal currentNav;
    private double allocationPercent;
    private BigDecimal profitOrLoss;
    private String fundSchemeId;
}