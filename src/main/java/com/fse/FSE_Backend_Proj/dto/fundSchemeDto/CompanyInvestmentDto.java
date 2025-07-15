package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CompanyInvestmentDto {
    private String id;
    private Long companyId;
    private String companyName;
    private BigDecimal investedAmount;
    private Integer numberOfStocks;
    private LocalDate investmentDate;
    private String fundSchemeId;
}
