package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.StrategyRequest;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveStrategyService {

    private final StrategyRepository strategyRepository;

    // Create or update
    public StrategyRequest saveStrategy(StrategyRequest dto) {
        Strategy strategy = Strategy.builder()
                .id(dto.getId()) // needed for update operations
                .name(dto.getStrategyName())
                .script(dto.getStrategyScript())
                .symbolList(dto.getSymbolList())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .initialCapital(dto.getInitialCapital())
                .symbol(dto.getSymbol())
                .status(dto.getStatus() != null ? dto.getStatus() : "not started")
                .resultJson(dto.getResultJson())
                .build();

        Strategy saved = strategyRepository.save(strategy);
        return mapToDTO(saved);
    }

    // Get all
    public List<StrategyRequest> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get by id
    public StrategyRequest getStrategyById(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        return mapToDTO(strategy);
    }

    // Delete
    public void deleteStrategy(Long id) {
        strategyRepository.deleteById(id);
    }

    // Start simulation
    public StrategyRequest startSimulation(Long id) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("running");
        return mapToDTO(strategyRepository.save(strategy));
    }

    // Stop simulation
    public StrategyRequest stopSimulation(Long id) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("stopped");
        return mapToDTO(strategyRepository.save(strategy));
    }

    // Complete simulation
    public StrategyRequest completeSimulation(Long id, String resultJson) {
        Strategy strategy = getStrategy(id);
        strategy.setStatus("completed");
        strategy.setResultJson(resultJson);
        return mapToDTO(strategyRepository.save(strategy));
    }

    // Helper to fetch Strategy or throw
    private Strategy getStrategy(Long id) {
        return strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
    }

    // Map entity -> DTO
    private StrategyRequest mapToDTO(Strategy strategy) {
        return StrategyRequest.builder()
                .id(strategy.getId()) // Add this
                .strategyName(strategy.getName())
                .strategyScript(strategy.getScript())
                .symbolList(strategy.getSymbolList())
                .startDate(strategy.getStartDate())
                .endDate(strategy.getEndDate())
                .initialCapital(strategy.getInitialCapital())
                .symbol(strategy.getSymbol())
                .status(strategy.getStatus())
                .resultJson(strategy.getResultJson())
                .build();
    }
}
