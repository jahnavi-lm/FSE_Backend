package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestmentRequest {
    private String investorId;
    private String schemeId;
    private BigDecimal amount;
}
