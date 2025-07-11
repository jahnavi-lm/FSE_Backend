package com.fse.FSE_Backend_Proj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyRequest {
    private Long id;
    private String strategyName;       // Optional: predefined strategy
    private String strategyScript;     // Optional: DSL-based script
    private List<String> symbolList;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double initialCapital;
    private String symbol;// Optional, default = 10000

    private String status;
    private String resultJson;
}
