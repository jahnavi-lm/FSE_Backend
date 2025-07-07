package com.fse.FSE_Backend_Proj.dto.strategyDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyDTO {

    private Long id;
    private String name;
    private String type;
    private Double capitalAllocation;
    private String status;
    private String parametersJson;
    private String resultJson;

}
