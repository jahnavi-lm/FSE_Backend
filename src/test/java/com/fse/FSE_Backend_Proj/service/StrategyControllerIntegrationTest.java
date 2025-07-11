package com.fse.FSE_Backend_Proj.service;//package com.fse.FSE_Backend_Proj.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
//import com.fse.FSE_Backend_Proj.model.Strategy;
//import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
//public class StrategyControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private StrategyRepository strategyRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Strategy strategy;
//
//    @BeforeEach
//    void setup() {
//        strategyRepository.deleteAll(); // Clean database before each test
//
//        strategy = Strategy.builder()
//                .name("Test Strategy")
//                .type("INTRADAY")
//                .capitalAllocation(100000.0)
//                .status("not started")
//                .parametersJson("{\"ma\":\"20\"}")
//                .resultJson(null)
//                .build();
//
//        strategy = strategyRepository.save(strategy);
//    }
//
//    @Test
//    void testGetAllStrategies() throws Exception {
//        mockMvc.perform(get("/api/strategies"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void testGetStrategyById() throws Exception {
//        mockMvc.perform(get("/api/strategies/" + strategy.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Strategy"));
//    }
//
//    @Test
//    void testSaveStrategy() throws Exception {
//        StrategyDTO dto = StrategyDTO.builder()
//                .name("New Strategy")
//                .type("SWING")
//                .capitalAllocation(50000.0)
//                .parametersJson("{\"param\":\"value\"}")
//                .build();
//
//        mockMvc.perform(post("/api/strategies")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("New Strategy"));
//    }
//
//    @Test
//    void testDeleteStrategy() throws Exception {
//        mockMvc.perform(delete("/api/strategies/" + strategy.getId()))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testStartSimulation() throws Exception {
//        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/start"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("running"));
//    }
//
//    @Test
//    void testStopSimulation() throws Exception {
//        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/stop"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("stopped"));
//    }
//
//    @Test
//    void testCompleteSimulation() throws Exception {
//        String resultJson = "{\"result\":\"profit\"}";
//
//        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/complete")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(resultJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("completed"))
//                .andExpect(jsonPath("$.resultJson").value(resultJson));
//    }
//}










//package com.fse.FSE_Backend_Proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyDTO;
import com.fse.FSE_Backend_Proj.model.Strategy;
import com.fse.FSE_Backend_Proj.repository.StrategyRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StrategyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StrategyRepository strategyRepository;

    private static final String JWT_SECRET = "fseprojectsecurekeymustbe32bytes!"; // 32 characters
    private Strategy strategy;

    private String generateJwt() {
        return Jwts.builder()
                .setSubject("testuser@example.com")
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    @BeforeEach
    void setup() {
        strategyRepository.deleteAll();

        strategy = Strategy.builder()
                .name("Test Strategy")
                .type("INTRADAY")
                .capitalAllocation(100000.0)
                .status("not started")
                .parametersJson("{\"ma\":\"20\"}")
                .resultJson(null)
                .build();

        strategy = strategyRepository.save(strategy);
    }

    @Test
    public void testCreateStrategy() throws Exception {
        StrategyDTO strategyDTO = StrategyDTO.builder()
                .name("New Strategy")
                .type("SWING")
                .capitalAllocation(50000.0)
                .status("not started")
                .parametersJson("{\"param\":\"value\"}")
                .build();

        String jwt = generateJwt();

        mockMvc.perform(post("/api/strategies")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(strategyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Strategy"));
    }

    @Test
    void testGetAllStrategies() throws Exception {
        String jwt = generateJwt();
        mockMvc.perform(get("/api/strategies")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetStrategyById() throws Exception {
        String jwt = generateJwt();
        mockMvc.perform(get("/api/strategies/" + strategy.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Strategy"));
    }

    @Test
    void testDeleteStrategy() throws Exception {
        String jwt = generateJwt();
        mockMvc.perform(delete("/api/strategies/" + strategy.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    @Test
    void testStartSimulation() throws Exception {
        String jwt = generateJwt();
        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/start")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("running"));
    }

    @Test
    void testStopSimulation() throws Exception {
        String jwt = generateJwt();
        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/stop")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("stopped"));
    }

    @Test
    void testCompleteSimulation() throws Exception {
        String jwt = generateJwt();
        String resultJson = "{\"result\":\"profit\"}";

        mockMvc.perform(post("/api/strategies/" + strategy.getId() + "/complete")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resultJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("completed"))
                .andExpect(jsonPath("$.resultJson").value(resultJson));
    }
}


