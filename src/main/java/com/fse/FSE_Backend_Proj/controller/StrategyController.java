package com.fse.FSE_Backend_Proj.controller;


import com.fse.FSE_Backend_Proj.dto.StrategyResponseDTO;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyRepository strategyRepository;

    @PostMapping
    public StrategyResponseDTO saveStrategy(@RequestBody Strategy strategy) {
        Strategy saved = strategyRepository.save(strategy);
        return toDTO(saved);
    }

    @GetMapping
    public List<StrategyResponseDTO> getAllStrategies() {
        return strategyRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StrategyResponseDTO getById(@PathVariable Long id) {
        return strategyRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Strategy not found"));
    }

    private StrategyResponseDTO toDTO(Strategy s) {
        return StrategyResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .symbol(s.getSymbol())
                .script(s.getScript())
                .paramsJson(s.getParamsJson())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .build();
    }
}
