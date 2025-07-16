package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.repository.BacktestResultRepository;
import com.fse.FSE_Backend_Proj.repository.SaveStrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final SaveStrategyRepository saveStrategyRepository;
    private final BacktestResultRepository backtestResultRepository;

    // ➤ Count of all saved strategies
    @GetMapping("/strategies/count")
    public ResponseEntity<Long> getStrategyCount() {
        long count = saveStrategyRepository.count();
        return ResponseEntity.ok(count);
    }

    // ➤ Count of all backtest results
    @GetMapping("/backtest-results/count")
    public ResponseEntity<Long> getBacktestResultCount() {
        long count = backtestResultRepository.count();
        return ResponseEntity.ok(count);
    }
}
