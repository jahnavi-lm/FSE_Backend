package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvestmentRequest {
    private String investorId;
    private String schemeId;
    private BigDecimal amount;


}
