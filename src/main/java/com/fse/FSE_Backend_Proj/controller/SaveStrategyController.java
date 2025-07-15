package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.BacktestResultDTO;
import com.fse.FSE_Backend_Proj.dto.StrategyRequest;
import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.model.BacktestResult;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.service.SaveStrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/save-strategies")
@RequiredArgsConstructor
public class SaveStrategyController {

    private final SaveStrategyService saveStrategyService;

    @GetMapping
    public ResponseEntity<List<StrategyRequest>> getAllStrategies() {
        return ResponseEntity.ok(saveStrategyService.getAllStrategies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrategyRequest> getStrategyById(@PathVariable Long id) {
        StrategyRequest strategy = saveStrategyService.getStrategyById(id);
        return ResponseEntity.ok(strategy);
    }

    @PostMapping
    public ResponseEntity<StrategyRequest> saveStrategy(@RequestBody StrategyRequest dto) {
        return ResponseEntity.ok(saveStrategyService.saveStrategy(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        saveStrategyService.deleteStrategy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<StrategyRequest> startSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(saveStrategyService.startSimulation(id));
    }


    @PostMapping("/{id}/stop")
    public ResponseEntity<StrategyRequest> stopSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(saveStrategyService.stopSimulation(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<StrategyRequest> completeSimulation(
            @PathVariable Long id,
            @RequestBody String resultJson
    ) {
        return ResponseEntity.ok(saveStrategyService.completeSimulation(id, resultJson));
    }
//    @GetMapping("/{id}/result")
//    public ResponseEntity<BacktestResultDTO> getResultByStrategyId(@PathVariable Long id) {
//        return ResponseEntity.ok(saveStrategyService.getBacktestResultForStrategy(id));
//    }
    @GetMapping("/result/{strategyId}")
    public ResponseEntity<BacktestResult> getResultByStrategyId(@PathVariable Long strategyId) {
        BacktestResult result = saveStrategyService.getBacktestResultByStrategyId(strategyId);
        return ResponseEntity.ok(result);
    }


}
