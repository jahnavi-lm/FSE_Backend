package com.fse.FSE_Backend_Proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
import com.fse.FSE_Backend_Proj.repository.InvestorRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FundManagerControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private FundManagerRepository fundManagerRepository;
    @Autowired private InvestorRepository investorRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String userId;

    @BeforeEach
    void setup() {
        // Clean up in correct FK order to prevent constraint violations
        fundManagerRepository.deleteAll();
        investorRepository.deleteAll();  // In case investors linked to user
        userRepository.deleteAll();

        User savedUser = User.builder()
                .name("Test Fund Manager")
                .email("fm@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .role(UserRole.MANAGER)
                .build();

        userId = userRepository.save(savedUser).getId();
    }

    @Test
    void testCreateAndGetFundManager() throws Exception {
        FundManagerRequestDto dto = FundManagerRequestDto.builder()
                .userId(userId)
                .employeeCode("EMP001")
                .qualification("MBA")
                .experienceYears(5)
                .bio("Experienced manager")
                .build();

        // 1. Create
        String response = mockMvc.perform(post("/api/fundManagers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCode").value("EMP001"))
                .andReturn().getResponse().getContentAsString();

        String fmId = objectMapper.readTree(response).get("id").asText();

        // 2. Get by ID
        mockMvc.perform(get("/api/fundManagers/" + fmId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fmId));

        // 3. Get All
        mockMvc.perform(get("/api/fundManagers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeCode").value("EMP001"));

        // 4. Exists
        mockMvc.perform(get("/api/fundManagers/exists/" + fmId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 5. Update
        dto.setQualification("CFA");
        mockMvc.perform(put("/api/fundManagers/" + fmId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qualification").value("CFA"));

        // 6. Delete
        mockMvc.perform(delete("/api/fundManagers/" + fmId))
                .andExpect(status().isNoContent());
    }
}
