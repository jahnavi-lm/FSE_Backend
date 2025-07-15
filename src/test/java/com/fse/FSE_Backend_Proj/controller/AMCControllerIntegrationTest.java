//package com.fse.FSE_Backend_Proj.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fse.FSE_Backend_Proj.dto.amcDto.AmcRequestDto;
//import com.fse.FSE_Backend_Proj.model.AMC;
//import com.fse.FSE_Backend_Proj.model.User;
//import com.fse.FSE_Backend_Proj.model.enums.UserRole;
//import com.fse.FSE_Backend_Proj.repository.AMCRepository;
//import com.fse.FSE_Backend_Proj.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)  // disables JWT filter for test
//public class AMCControllerIntegrationTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private AMCRepository amcRepository;
//    @Autowired private UserRepository userRepository;
//    @Autowired private ObjectMapper objectMapper;
//
//    private AMC existingAmc;
//    private String userId;
//
//    @BeforeEach
//    void setUp() {
//        amcRepository.deleteAll();
//        userRepository.deleteAll();
//
//        User user = User.builder()
//                .id(UUID.randomUUID().toString())
//                .email("testamc@example.com")
//                .passwordHash("password")
//                .role(UserRole.valueOf("AMC"))
//                .build();
//
//        userId = userRepository.save(user).getId();
//
//        existingAmc = AMC.builder()
//                .id(userId)
//                .user(user)
//                .name("Test AMC")
//                .registrationNo("REG123456")
//                .contactEmail("testamc@example.com")
//                .contactPhone("9876543210")
//                .officeAddress("Mumbai, Maharashtra")
//                .build();
//
//        existingAmc = amcRepository.save(existingAmc);
//    }
//
//    @Test
//    void testCreateAmc() throws Exception {
//        AmcRequestDto request = AmcRequestDto.builder()
//                .name("New AMC")
//                .registrationNo("NEWREG456")
//                .contactEmail("newamc@example.com")
//                .contactPhone("9123456789")
//                .officeAddress("Pune, Maharashtra")
//                .build();
//
//        mockMvc.perform(post("/api/amcs/create/" + userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("New AMC"));
//    }
//
//    @Test
//    void testGetAmcById() throws Exception {
//        mockMvc.perform(get("/api/amcs/" + existingAmc.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test AMC"));
//    }
//
//    @Test
//    void testCheckFundManagerExists() throws Exception {
//        mockMvc.perform(get("/api/amcs/exists/" + existingAmc.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("true"));
//    }
//
//    @Test
//    void testGetAllAmcs() throws Exception {
//        mockMvc.perform(get("/api/amcs"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void testUpdateAmc() throws Exception {
//        AmcRequestDto updateRequest = AmcRequestDto.builder()
//                .name("Updated AMC")
//                .registrationNo("REG654321")
//                .contactEmail("updated@example.com")
//                .contactPhone("9999999999")
//                .officeAddress("Delhi NCR")
//                .build();
//
//        mockMvc.perform(put("/api/amcs/" + existingAmc.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated AMC"));
//    }
//
//    @Test
//    void testDeleteAmc() throws Exception {
//        mockMvc.perform(delete("/api/amcs/" + existingAmc.getId()))
//                .andExpect(status().isNoContent());
//    }
//}