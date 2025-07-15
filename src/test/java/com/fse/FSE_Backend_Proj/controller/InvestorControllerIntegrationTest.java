package com.fse.FSE_Backend_Proj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.FundScheme;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.model.enums.*;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
import com.fse.FSE_Backend_Proj.repository.InvestorRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.util.JWTUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InvestorControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private InvestorRepository investorRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private AMCRepository amcRepository;
    @Autowired private FundManagerRepository fundManagerRepository;

    private String jwtToken;
    private String userId;

    @BeforeEach
    void setUp() {
        fundManagerRepository.deleteAll(); // 👈 Clear dependent records first
        investorRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder()
                .name("Test Investor")
                .email("investor@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .role(UserRole.INVESTOR)
                .build();

        userId = userRepository.save(user).getId();

        jwtToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    @Test
    void testCreateInvestorAndGetSummary() throws Exception {
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV1234")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("123 Street")
                .guardianName("John Doe")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(750000.0))
                .nomineeName("Jane Doe")
                .bankAccountNo("123456789012")
                .ifscCode("SBIN0001234")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.panNumber").value("PANINV1234"))
                .andExpect(jsonPath("$.walletBalance").value(2000000.0));

        // Optional: Call summary endpoint
        mockMvc.perform(get("/api/investors/summary/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletBalance").value(2000000.0))
                .andExpect(jsonPath("$.totalReturns").value(0.0));
    }
    @Test
    void testGetInvestmentSummary() throws Exception {
        // Pre-create investor first (reuse logic if needed)
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV1234")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("123 Street")
                .guardianName("John Doe")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(750000.0))
                .nomineeName("Jane Doe")
                .bankAccountNo("123456789012")
                .ifscCode("SBIN0001234")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Now call summary API
        mockMvc.perform(get("/api/investors/summary/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletBalance").value(2000000.0))
                .andExpect(jsonPath("$.totalReturns").value(0.0))
                .andExpect(jsonPath("$.totalInvested").value(0.0));
    }

    @Test
    void testInvestorKycVerification() throws Exception {
        // Create the investor first
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV1234")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("123 Street")
                .guardianName("John Doe")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(750000.0))
                .nomineeName("Jane Doe")
                .bankAccountNo("123456789012")
                .ifscCode("SBIN0001234")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Verify KYC
        KycVerificationRequest kycRequest = KycVerificationRequest.builder()
                .investorId(userId)
                .build();

        mockMvc.perform(post("/api/investors/kyc/verify")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kycRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("KYC verified successfully"))
                .andExpect(jsonPath("$.kycStatus").value(true));
    }
    @Test
    void testGetWalletValue() throws Exception {
        // First, create the investor
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV1234")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("123 Street")
                .guardianName("John Doe")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(750000.0))
                .nomineeName("Jane Doe")
                .bankAccountNo("123456789012")
                .ifscCode("SBIN0001234")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Then, fetch wallet value
        mockMvc.perform(get("/api/investors/wallet-value/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletValue").value(2000000.0));
    }

    @Test
    void testGetInvestorProfile() throws Exception {
        // Step 1: Create investor
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV1234")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("123 Street")
                .guardianName("John Doe")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(750000.0))
                .nomineeName("Jane Doe")
                .bankAccountNo("123456789012")
                .ifscCode("SBIN0001234")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Step 2: Get investor profile
        mockMvc.perform(get("/api/investors/profile/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Test Investor"))
                .andExpect(jsonPath("$.email").value("investor@example.com"))
                .andExpect(jsonPath("$.kycStatus").value(false));
    }

    @Test
    void testGetPortfolio_handlesEmptyAndPopulatedPortfolio() throws Exception {
        // Step 1: Create investor
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV7777")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("789 Ocean Drive")
                .guardianName("Ella Doe")
                .occupation("Teacher")
                .annualIncome(BigDecimal.valueOf(450000.0))
                .nomineeName("Tommy Lee")
                .bankAccountNo("998877665544")
                .ifscCode("SBIN0033445")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Step 2: Get portfolio
        mockMvc.perform(get("/api/investors/portfolio/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.investorId").value(userId))
                .andExpect(jsonPath("$.portfolio").isArray())
                .andExpect(jsonPath("$.portfolio.length()").value(0)); // ✅ Assert empty array if no investments
    }

    @Autowired
    private com.fse.FSE_Backend_Proj.repository.FundSchemeRepository fundSchemeRepository;

    @PersistenceContext
    private EntityManager entityManager;


//    @Transactional
//    @Test
//    void testInvestorCreationFlowWithoutDuplicateEmail() throws Exception {
//        // ✅ Step 1: Unique Email and ID
//        String uniqueEmail = "investor_" + UUID.randomUUID() + "@example.com";
//        String userId = UUID.randomUUID().toString();
//
//        // ✅ Step 2: Create user before investor
//        User newUser = User.builder()
//                .id(userId)
//                .name("Test Investor")
//                .email(uniqueEmail)
//                .passwordHash(passwordEncoder.encode("password123"))
//                .role(UserRole.INVESTOR)
//                .status(UserStatus.ACTIVE)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        userRepository.save(newUser);
//
//        // ✅ Step 3: Prepare Investor request with unique PAN
//        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
//                .userId(userId)
//                .dob(LocalDate.of(1990, 1, 1))
//                .panNumber("PAN" + new Random().nextInt(1000000)) // Ensure uniqueness
//                .kycDocUrl("http://dummy.com/kyc.pdf")
//                .address("99 Sunset Blvd")
//                .guardianName("Peter Parker")
//                .occupation("Photographer")
//                .annualIncome(BigDecimal.valueOf(500000.0))
//                .nomineeName("Aunt May")
//                .bankAccountNo("321654987012")
//                .ifscCode("SBIN0055667")
//                .build();
//
//        // ✅ Step 4: Call the /create endpoint
//        mockMvc.perform(post("/api/investors/create")
//                        .header("Authorization", "Bearer " + jwtToken) // Make sure jwtToken maps to above user
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.user.id").value(userId))
//                .andExpect(jsonPath("$.panNumber").value(createRequest.getPanNumber()))
//                .andExpect(jsonPath("$.kycStatus").value(false));
//    }

    @Test
    void testGetKycStatus() throws Exception {
        // Step 1: Create the investor
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV5678")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("456 New Lane")
                .guardianName("Tony Stark")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(850000.0))
                .nomineeName("Pepper Potts")
                .bankAccountNo("112233445566")
                .ifscCode("HDFC0009876")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Step 2: Check KYC status (should be false initially)
        mockMvc.perform(get("/api/investors/kyc-status/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.investorId").value(userId))
                .andExpect(jsonPath("$.kycStatus").value(false));
    }

    @Test
    void testCheckInvestorExists() throws Exception {
        // Step 1: Create Investor
        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
                .userId(userId)
                .dob(LocalDate.of(1990, 1, 1))
                .panNumber("PANINV9999")
                .kycDocUrl("http://dummy.com/kyc.pdf")
                .address("Victory Road")
                .guardianName("Bruce Wayne")
                .occupation("Businessman")
                .annualIncome(BigDecimal.valueOf(1000000.0))
                .nomineeName("Alfred")
                .bankAccountNo("555544443333")
                .ifscCode("ICIC0001111")
                .build();

        mockMvc.perform(post("/api/investors/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Step 2: Check if investor exists
        mockMvc.perform(get("/api/investors/exists/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

//    @Test
//    void testGetNavHistory() throws Exception {
//        // Step 1: Create Investor
//        InvestorCreateRequest createRequest = InvestorCreateRequest.builder()
//                .userId(userId)
//                .dob(LocalDate.of(1990, 1, 1))
//                .panNumber("PANINV4321")
//                .kycDocUrl("http://dummy.com/kyc.pdf")
//                .address("Galaxy Avenue")
//                .guardianName("Leia Organa")
//                .occupation("Commander")
//                .annualIncome(BigDecimal.valueOf(950000.0))
//                .nomineeName("Han Solo")
//                .bankAccountNo("667788990011")
//                .ifscCode("PNB0002345")
//                .build();
//
//        mockMvc.perform(post("/api/investors/create")
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequest)))
//                .andExpect(status().isOk());
//
//        // Step 2: Create AMC
//        AMC amc = AMC.builder()
//                .user(userRepository.findById(userId).get())
//                .name("Galactic AMC")
//                .registrationNo("AMC00042")
//                .contactEmail("contact@amc.com")
//                .contactPhone("9999999999")
//                .officeAddress("Moon Base 1")
//                .build();
//        AMC savedAmc = amcRepository.save(amc);
//
//        // Step 3: Create Fund Scheme
//        FundScheme scheme = FundScheme.builder()
//                .name("Galaxy Growth Fund")
//                .type(FundSchemeType.EQUITY)
//                .objective("Expand the empire")
//                .aum(BigDecimal.valueOf(10000000))
//                .currentNav(BigDecimal.valueOf(150.25))
//                .riskLevel(RiskLevel.HIGH)
//                .expenseRatio(BigDecimal.valueOf(1.2))
//                .exitLoad(BigDecimal.valueOf(0.5))
//                .lockInPeriod(3)
//                .minInvestment(BigDecimal.valueOf(5000))
//                .minSipAmount(BigDecimal.valueOf(1000))
//                .amc(savedAmc)
//                .benchmarkIndex("S&P 500")
//                .launchDate(LocalDate.of(2022, 1, 1))
//                .category("Aggressive Equity")
//                .status(FundSchemeStatus.ACTIVE)
//                .build();
//
//        String schemeId = fundSchemeRepository.save(scheme).getId();
//
//        // Step 4: (Optional) Add NAV History — skip for now if default data is populated
//
//        // Step 5: Call NAV History endpoint
//        mockMvc.perform(get("/api/investors/nav-history/" + schemeId)
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray()); // just checking array is returned
//    }

//    @Test
//    void testGetAllAvailableSchemes() throws Exception {
//        // Reattach user for AMC
//        User userRef = entityManager.getReference(User.class, userId);
//
//        // Create AMC
//        AMC amc = AMC.builder()
//                .user(userRef)
//                .name("Eagle AMC")
//                .registrationNo("REG555")
//                .contactEmail("contact@eagleamc.com")
//                .contactPhone("9000000000")
//                .officeAddress("Eagle Tower")
//                .build();
//        AMC savedAmc = amcRepository.save(amc);
//
//        // Create two Fund Schemes (one ACTIVE, one INACTIVE)
//        FundScheme activeScheme = FundScheme.builder()
//                .name("Growth Fund")
//                .type(FundSchemeType.HYBRID)
//                .objective("Capital appreciation")
//                .aum(BigDecimal.valueOf(6000000))
//                .currentNav(BigDecimal.valueOf(105.75))
//                .riskLevel(RiskLevel.HIGH)
//                .expenseRatio(BigDecimal.valueOf(1.1))
//                .exitLoad(BigDecimal.valueOf(0.6))
//                .lockInPeriod(1)
//                .minInvestment(BigDecimal.valueOf(2000))
//                .minSipAmount(BigDecimal.valueOf(500))
//                .amc(savedAmc)
//                .benchmarkIndex("Nifty 200")
//                .launchDate(LocalDate.of(2023, 3, 1))
//                .category("Growth")
//                .status(FundSchemeStatus.ACTIVE)
//                .build();
//
//        FundScheme inactiveScheme = FundScheme.builder()
//                .name("Legacy Fund")
//                .type(FundSchemeType.EQUITY)
//                .objective("Preservation")
//                .aum(BigDecimal.valueOf(4000000))
//                .currentNav(BigDecimal.valueOf(95.50))
//                .riskLevel(RiskLevel.LOW)
//                .expenseRatio(BigDecimal.valueOf(0.7))
//                .exitLoad(BigDecimal.valueOf(0.4))
//                .lockInPeriod(1)
//                .minInvestment(BigDecimal.valueOf(1500))
//                .minSipAmount(BigDecimal.valueOf(400))
//                .amc(savedAmc)
//                .benchmarkIndex("Sensex")
//                .launchDate(LocalDate.of(2022, 7, 1))
//                .category("Stable")
//                .status(FundSchemeStatus.CLOSED) //
//                .build();
//
//        fundSchemeRepository.save(activeScheme);
//        fundSchemeRepository.save(inactiveScheme);
//
//        // Call endpoint
//        mockMvc.perform(get("/api/investors/available-schemes")
//                        .header("Authorization", "Bearer " + jwtToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1)) // ✅ Only 1 active
//                .andExpect(jsonPath("$[0].name").value("Growth Fund"))
//                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
//    }


}
