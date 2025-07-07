package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;

    public StrategyDTO saveStrategy(StrategyDTO dto) {
        Strategy strategy = Strategy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .capitalAllocation(dto.getCapitalAllocation())
                .status(dto.getStatus() != null ? dto.getStatus() : "not started")
                .parametersJson(dto.getParametersJson())
                .resultJson(dto.getResultJson())
                .build();

        Strategy saved = strategyRepository.save(strategy);
        return mapToDTO(saved);
    }

    public List<StrategyDTO> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StrategyDTO getStrategyById(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        return mapToDTO(strategy);
    }

    public void deleteStrategy(Long id) {
        strategyRepository.deleteById(id);
    }

    public StrategyDTO startSimulation(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        strategy.setStatus("running");
        return mapToDTO(strategyRepository.save(strategy));
    }

    public StrategyDTO stopSimulation(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        strategy.setStatus("stopped");
        return mapToDTO(strategyRepository.save(strategy));
    }

    public StrategyDTO completeSimulation(Long id, String resultJson) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        strategy.setStatus("completed");
        strategy.setResultJson(resultJson);
        return mapToDTO(strategyRepository.save(strategy));
    }

    private StrategyDTO mapToDTO(Strategy strategy) {
        return StrategyDTO.builder()
                .id(strategy.getId())
                .name(strategy.getName())
                .type(strategy.getType())
                .capitalAllocation(strategy.getCapitalAllocation())
                .status(strategy.getStatus())
                .parametersJson(strategy.getParametersJson())
                .resultJson(strategy.getResultJson())
                .build();
    }
}
