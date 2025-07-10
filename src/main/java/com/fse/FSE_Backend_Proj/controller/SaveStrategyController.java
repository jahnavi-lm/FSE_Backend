package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
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
    public ResponseEntity<List<StrategyDTO>> getAllStrategies() {
        return ResponseEntity.ok(saveStrategyService.getAllStrategies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StrategyDTO> getStrategyById(@PathVariable Long id) {
        StrategyDTO strategy = saveStrategyService.getStrategyById(id);
        return ResponseEntity.ok(strategy);
    }

    @PostMapping
    public ResponseEntity<StrategyDTO> saveStrategy(@RequestBody StrategyDTO dto) {
        return ResponseEntity.ok(saveStrategyService.saveStrategy(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        saveStrategyService.deleteStrategy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<StrategyDTO> startSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(saveStrategyService.startSimulation(id));
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<StrategyDTO> stopSimulation(@PathVariable Long id) {
        return ResponseEntity.ok(saveStrategyService.stopSimulation(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<StrategyDTO> completeSimulation(
            @PathVariable Long id,
            @RequestBody String resultJson
    ) {
        return ResponseEntity.ok(saveStrategyService.completeSimulation(id, resultJson));
    }
}
