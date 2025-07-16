package com.fse.FSE_Backend_Proj.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailsDto {
    private Long id;
    private String symbol;
    private String name;
    private String indexName;
    private BigDecimal totalCapital;
    private BigDecimal nav;
    private Double riskFactor;
    private String fundSchemeId;      // Optional: If company is linked to a scheme
    private String fundSchemeName;
}
