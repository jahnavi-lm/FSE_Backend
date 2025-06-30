package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RedeemRequest {
    private String investorId;
    private UUID schemeId;
    private double unitsToRedeem;


}
