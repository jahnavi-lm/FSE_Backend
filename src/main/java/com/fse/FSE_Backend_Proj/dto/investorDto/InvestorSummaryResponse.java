package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InvestorSummaryResponse {
    private BigDecimal totalInvested;
    private BigDecimal currentValue;
    private BigDecimal totalReturns;
    private double walletBalance;
}
