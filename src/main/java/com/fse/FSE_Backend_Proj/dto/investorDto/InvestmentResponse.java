package com.fse.FSE_Backend_Proj.dto.investorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class InvestmentResponse {
    private String message;
    private double unitsAllocated;
    private double navAtPurchase;
    private LocalDateTime txnTime;



}
