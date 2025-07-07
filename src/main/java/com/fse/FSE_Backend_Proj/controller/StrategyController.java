package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.service.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping
    public ResponseEntity<List<StrategyDTO>> getAllStrategies() {
        return ResponseEntity.ok(strategyService.getAllStrategies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrategyDTO> getStrategyById(@PathVariable Long id) {
        StrategyDTO strategy = strategyService.getStrategyById(id);
        return ResponseEntity.ok(strategy);
    }

    @PostMapping
    public ResponseEntity<StrategyDTO> saveStrategy(@RequestBody StrategyDTO dto) {
        return ResponseEntity.ok(strategyService.saveStrategy(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        strategyService.deleteStrategy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<StrategyDTO> startSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(strategyService.startSimulation(id));
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<StrategyDTO> stopSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(strategyService.stopSimulation(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<StrategyDTO> completeSimulation(
            @PathVariable Long id,
            @RequestBody String resultJson
    ) {
        return ResponseEntity.ok(strategyService.completeSimulation(id, resultJson));
    }
}
