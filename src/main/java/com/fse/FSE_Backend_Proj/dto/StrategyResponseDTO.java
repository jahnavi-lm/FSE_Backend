package com.fse.FSE_Backend_Proj.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyResponseDTO {
    private Long id;
    private String name;
    private String symbol;
    private String script;         // DSL or empty for predefined
    private String paramsJson;     // Optional config for predefined
    private LocalDate startDate;
    private LocalDate endDate;
}
