package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StrategyServiceTest {

    @Mock
    private StrategyRepository strategyRepository;

    @InjectMocks
    private StrategyService strategyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private StrategyDTO getSampleDTO() {
        return StrategyDTO.builder()
                .id(1L)
                .name("Momentum Strategy")
                .type("Equity")
                .capitalAllocation(500000.0)
                .status("running")
                .parametersJson("{\"lookback\": 20}")
                .resultJson(null)
                .build();
    }

    private Strategy getSampleEntity() {
        return Strategy.builder()
                .id(1L)
                .name("Momentum Strategy")
                .type("Equity")
                .capitalAllocation(500000.0)
                .status("running")
                .parametersJson("{\"lookback\": 20}")
                .resultJson(null)
                .build();
    }

    // Helper to stub mapToDTO behavior
    private StrategyDTO mapToDTO(Strategy s) {
        return StrategyDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .type(s.getType())
                .capitalAllocation(s.getCapitalAllocation())
                .status(s.getStatus())
                .parametersJson(s.getParametersJson())
                .resultJson(s.getResultJson())
                .build();
    }

    // --- Test saveStrategy() ---

    @Test
    void testSaveStrategy_success() {
        StrategyDTO dto = getSampleDTO();
        Strategy strategyToSave = getSampleEntity();

        when(strategyRepository.save(any(Strategy.class))).thenReturn(strategyToSave);

        // manually inject mapToDTO if needed
        StrategyDTO result = strategyService.saveStrategy(dto);

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getType(), result.getType());
        verify(strategyRepository, times(1)).save(any(Strategy.class));
    }

    @Test
    void testSaveStrategy_setsDefaultStatusIfNull() {
        StrategyDTO dto = getSampleDTO();
        dto.setStatus(null); // No status provided

        Strategy strategyWithDefaultStatus = getSampleEntity();
        strategyWithDefaultStatus.setStatus("not started");

        when(strategyRepository.save(any(Strategy.class))).thenReturn(strategyWithDefaultStatus);

        StrategyDTO result = strategyService.saveStrategy(dto);

        assertNotNull(result);
        assertEquals("not started", result.getStatus());
    }

    @Test
    void testSaveStrategy_nullFieldsHandled() {
        StrategyDTO dto = StrategyDTO.builder()
                .name("Test")
                .build();

        Strategy savedEntity = Strategy.builder()
                .id(99L)
                .name("Test")
                .status("not started")
                .build();

        when(strategyRepository.save(any(Strategy.class))).thenReturn(savedEntity);

        StrategyDTO result = strategyService.saveStrategy(dto);
        assertEquals("Test", result.getName());
        assertEquals("not started", result.getStatus());
    }

    // --- Test getAllStrategies() ---

    @Test
    void testGetAllStrategies_returnsList() {
        Strategy s1 = getSampleEntity();
        Strategy s2 = Strategy.builder()
                .id(2L)
                .name("Mean Reversion")
                .type("Equity")
                .capitalAllocation(300000.0)
                .status("completed")
                .parametersJson("{\"threshold\": 0.05}")
                .build();

        when(strategyRepository.findAll()).thenReturn(List.of(s1, s2));

        List<StrategyDTO> result = strategyService.getAllStrategies();

        assertEquals(2, result.size());
        assertEquals("Momentum Strategy", result.get(0).getName());
        assertEquals("Mean Reversion", result.get(1).getName());
    }

    @Test
    void testGetAllStrategies_emptyList() {
        when(strategyRepository.findAll()).thenReturn(List.of());

        List<StrategyDTO> result = strategyService.getAllStrategies();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetStrategyById_success() {
        Strategy sample = getSampleEntity();
        when(strategyRepository.findById(1L)).thenReturn(Optional.of(sample));

        StrategyDTO result = strategyService.getStrategyById(1L);

        assertNotNull(result);
        assertEquals("Momentum Strategy", result.getName());
        verify(strategyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStrategyById_notFound() {
        when(strategyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategyService.getStrategyById(1L));
        assertEquals("Strategy not found", ex.getMessage());
    }

    @Test
    void testDeleteStrategy_success() {
        doNothing().when(strategyRepository).deleteById(1L);

        strategyService.deleteStrategy(1L);

        verify(strategyRepository, times(1)).deleteById(1L);
    }

    @Test
    void testStartSimulation_success() {
        Strategy sample = getSampleEntity();
        sample.setStatus("not started");

        when(strategyRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(strategyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StrategyDTO result = strategyService.startSimulation(1L);

        assertEquals("running", result.getStatus());
        verify(strategyRepository).save(any());
    }

    @Test
    void testStopSimulation_success() {
        Strategy sample = getSampleEntity();
        sample.setStatus("running");

        when(strategyRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(strategyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StrategyDTO result = strategyService.stopSimulation(1L);

        assertEquals("stopped", result.getStatus());
        verify(strategyRepository).save(any());
    }

    @Test
    void testCompleteSimulation_success() {
        Strategy sample = getSampleEntity();
        String resultJson = "{\"profit\": 5000}";

        when(strategyRepository.findById(1L)).thenReturn(Optional.of(sample));
        when(strategyRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        StrategyDTO result = strategyService.completeSimulation(1L, resultJson);

        assertEquals("completed", result.getStatus());
        assertEquals(resultJson, result.getResultJson());
        verify(strategyRepository).save(any());
    }

    @Test
    void testStartSimulation_strategyNotFound() {
        when(strategyRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategyService.startSimulation(99L));
        assertEquals("Strategy not found", ex.getMessage());
    }

    @Test
    void testStopSimulation_strategyNotFound() {
        when(strategyRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategyService.stopSimulation(99L));
        assertEquals("Strategy not found", ex.getMessage());
    }

    @Test
    void testCompleteSimulation_strategyNotFound() {
        when(strategyRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> strategyService.completeSimulation(99L, "{}"));
        assertEquals("Strategy not found", ex.getMessage());
    }


}
