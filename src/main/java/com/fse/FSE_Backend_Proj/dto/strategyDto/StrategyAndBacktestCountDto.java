package com.fse.FSE_Backend_Proj.dto.strategyDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategyAndBacktestCountDto {
    private long strategyCount;
    private long backtestCount;
}
