package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoldingDto {
    private String schemeId;
    private String schemeName;
    private double unitsHeld;
    private double currentNav;
    private double currentValue;
}
