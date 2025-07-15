package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.controller.StrategyController;
import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StrategyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StrategyService strategyService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        StrategyController controller = new StrategyController(strategyService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    private StrategyDTO sampleStrategy() {
        return StrategyDTO.builder()
                .id(1L)
                .name("Test Strategy")
                .type("TypeA")
                .capitalAllocation(1000.0)
                .status("not started")
                .parametersJson("{}")
                .resultJson(null)
                .build();
    }

    @Test
    void getAllStrategies_returnsList() throws Exception {
        List<StrategyDTO> strategies = Arrays.asList(sampleStrategy());
        Mockito.when(strategyService.getAllStrategies()).thenReturn(strategies);
        mockMvc.perform(get("/api/strategies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllStrategies_returnsEmptyList() throws Exception {
        Mockito.when(strategyService.getAllStrategies()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/strategies"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getStrategyById_found() throws Exception {
        Mockito.when(strategyService.getStrategyById(1L)).thenReturn(sampleStrategy());
        mockMvc.perform(get("/api/strategies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getStrategyById_notFound() throws Exception {
        Mockito.when(strategyService.getStrategyById(2L)).thenThrow(new RuntimeException("Strategy not found"));
        mockMvc.perform(get("/api/strategies/2"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void saveStrategy_success() throws Exception {
        StrategyDTO dto = sampleStrategy();
        Mockito.when(strategyService.saveStrategy(any())).thenReturn(dto);
        mockMvc.perform(post("/api/strategies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void saveStrategy_invalidInput() throws Exception {
        mockMvc.perform(post("/api/strategies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteStrategy_success() throws Exception {
        Mockito.doNothing().when(strategyService).deleteStrategy(1L);
        mockMvc.perform(delete("/api/strategies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void startSimulation_success() throws Exception {
        Mockito.when(strategyService.startSimulation(1L)).thenReturn(sampleStrategy());
        mockMvc.perform(post("/api/strategies/1/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void stopSimulation_success() throws Exception {
        Mockito.when(strategyService.stopSimulation(1L)).thenReturn(sampleStrategy());
        mockMvc.perform(post("/api/strategies/1/stop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void completeSimulation_success() throws Exception {
        Mockito.when(strategyService.completeSimulation(eq(1L), any())).thenReturn(sampleStrategy());
        mockMvc.perform(post("/api/strategies/1/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"result\":\"success\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void completeSimulation_notFound() throws Exception {
        Mockito.when(strategyService.completeSimulation(eq(2L), any())).thenThrow(new RuntimeException("Strategy not found"));
        mockMvc.perform(post("/api/strategies/2/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"result\":\"fail\"}"))
                .andExpect(status().isInternalServerError());
    }

    // JWT edge cases are not handled in standalone setup unless you add the filter manually.
    // You can add JWTAuthFilter as a mock if you want to test filter behavior.
}