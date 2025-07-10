package com.fse.FSE_Backend_Proj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDTO {
    private String date;
    private String action; // BUY or SELL
    private double price;

    // NEW FIELDS for trade table
    private String symbol;
    private int quantity;
    private double totalCostPrice;
    private double openingBalance;
    private double closingBalance;
    private double nav;
    private double realizedProfit;
}
