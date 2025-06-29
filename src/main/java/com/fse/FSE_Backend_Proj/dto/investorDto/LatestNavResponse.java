package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LatestNavResponse {
    private String schemeId;
    private BigDecimal nav;
    private LocalDate navDate;
}
