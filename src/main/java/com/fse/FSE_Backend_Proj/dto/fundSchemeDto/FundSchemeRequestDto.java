package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import com.fse.FSE_Backend_Proj.model.enums.FundSchemeStatus;
import com.fse.FSE_Backend_Proj.model.enums.FundSchemeType;
import com.fse.FSE_Backend_Proj.model.enums.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FundSchemeRequestDto {

    @NotBlank
    private String name;
    @NotNull
    private FundSchemeType type;
    @NotBlank private String objective;
    private BigDecimal aum;
    private BigDecimal currentNav;
    private RiskLevel riskLevel;
    private BigDecimal expenseRatio;
    private BigDecimal exitLoad;
    private Integer lockInPeriod;
    @NotNull private BigDecimal minInvestment;
    private BigDecimal minSipAmount;
    @NotBlank private String benchmarkIndex;
    private LocalDate launchDate;
    @NotBlank private String category;
    @NotNull private FundSchemeStatus status;
}
