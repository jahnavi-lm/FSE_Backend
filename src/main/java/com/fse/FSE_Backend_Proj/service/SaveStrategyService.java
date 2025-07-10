package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.model.SaveStrategy;
import com.fse.FSE_Backend_Proj.repository.SaveStrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveStrategyService {

    private final SaveStrategyRepository saveStrategyRepository;

    public StrategyDTO saveStrategy(StrategyDTO dto) {
        SaveStrategy saveStrategy = SaveStrategy.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .capitalAllocation(dto.getCapitalAllocation())
                .status(dto.getStatus() != null ? dto.getStatus() : "not started")
                .parametersJson(dto.getParametersJson())
                .resultJson(dto.getResultJson())
                .build();

        SaveStrategy saved = saveStrategyRepository.save(saveStrategy);
        return mapToDTO(saved);
    }

    public List<StrategyDTO> getAllStrategies() {
        return saveStrategyRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StrategyDTO getStrategyById(Long id) {
        SaveStrategy saveStrategy = saveStrategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        return mapToDTO(saveStrategy);
    }

    public void deleteStrategy(Long id) {
        saveStrategyRepository.deleteById(id);
    }

    public StrategyDTO startSimulation(Long id) {
        SaveStrategy saveStrategy = saveStrategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        saveStrategy.setStatus("running");
        return mapToDTO(saveStrategyRepository.save(saveStrategy));
    }

    public StrategyDTO stopSimulation(Long id) {
        SaveStrategy saveStrategy = saveStrategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        saveStrategy.setStatus("stopped");
        return mapToDTO(saveStrategyRepository.save(saveStrategy));
    }

    public StrategyDTO completeSimulation(Long id, String resultJson) {
        SaveStrategy saveStrategy = saveStrategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
        saveStrategy.setStatus("completed");
        saveStrategy.setResultJson(resultJson);
        return mapToDTO(saveStrategyRepository.save(saveStrategy));
    }

    private StrategyDTO mapToDTO(SaveStrategy saveStrategy) {
        return StrategyDTO.builder()
                .id(saveStrategy.getId())
                .name(saveStrategy.getName())
                .type(saveStrategy.getType())
                .capitalAllocation(saveStrategy.getCapitalAllocation())
                .status(saveStrategy.getStatus())
                .parametersJson(saveStrategy.getParametersJson())
                .resultJson(saveStrategy.getResultJson())
                .build();
    }
}
