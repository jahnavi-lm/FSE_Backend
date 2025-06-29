package com.fse.FSE_Backend_Proj.dto.fundSchemeDto;

import com.fse.FSE_Backend_Proj.model.enums.FundSchemeStatus;
import com.fse.FSE_Backend_Proj.model.enums.FundSchemeType;
import com.fse.FSE_Backend_Proj.model.enums.RiskLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FundSchemeResponseDto {
    private String id;
    private String name;
    private FundSchemeType type;
    private String objective;
    private BigDecimal aum;
    private BigDecimal currentNav;
    private RiskLevel riskLevel;
    private BigDecimal expenseRatio;
    private BigDecimal exitLoad;
    private Integer lockInPeriod;
    private BigDecimal minInvestment;
    private BigDecimal minSipAmount;
    private String benchmarkIndex;
    private LocalDate launchDate;
    private String category;
    private FundSchemeStatus status;
    private String amcId;
    private String managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
