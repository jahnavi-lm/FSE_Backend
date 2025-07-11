package com.fse.FSE_Backend_Proj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BacktestResultDTO {
    private double initialEquity;
    private double finalEquity;
    private int totalTrades;
    private List<TradeDTO> trades;
}
