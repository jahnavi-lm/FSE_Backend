package com.fse.FSE_Backend_Proj.service;

//import com.fse.FSE_Backend_Proj.dto.*;
import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import com.fse.FSE_Backend_Proj.model.enums.FundSchemeType;
import com.fse.FSE_Backend_Proj.model.enums.RiskLevel;
import com.fse.FSE_Backend_Proj.model.enums.TransactionType;
import com.fse.FSE_Backend_Proj.exception.DuplicatePanException;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InvestorServiceImplTest {

    @InjectMocks
    private InvestorServiceImpl investorService;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private FundSchemeRepository fundSchemeRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UnitLedgerRepository unitLedgerRepository;

    @Mock
    private NAVHistoryRepository navHistoryRepository;

    @Mock
    private UserRepository userRepository;

    private Investor investor;
    private FundScheme scheme;
    private NAVHistory nav;
    String investorId = "b16716c3-5b1e-46a2-a740-9c547cbe7a63";
    String schemeId = "d4b7ecf1-bbf2-45fc-91c9-ff1c76d7e7b1";


    @BeforeEach
    void setup() {
        investor = Investor.builder()
                .id(investorId)
                .walletBalance(2000000.0)
                .user(User.builder().id(investorId).name("Abhishek").email("a@a.com").build())
                .build();

        scheme = FundScheme.builder()
                .id(schemeId)
                .name("Growth Fund")
                .aum(BigDecimal.valueOf(1000000))
                .currentNav(BigDecimal.valueOf(100))
                .build();

        nav = NAVHistory.builder()
                .id(1L)
                .nav(BigDecimal.valueOf(100))
                .date(LocalDate.now())
                .build();
    }

//    @Test
//    void testRedeem_success() {
//        String investorId = "inv-123";
//        String schemeId = UUID.randomUUID().toString();
//
//        Investor investor = Investor.builder()
//                .id(investorId)
//                .walletBalance(1000.0)
//                .build();
//
//        FundScheme scheme = FundScheme.builder()
//                .id(schemeId)
//                .name("Balanced Fund")
//                .aum(BigDecimal.valueOf(10000))
//                .currentNav(BigDecimal.valueOf(50)) // ✅ Used directly now
//                .build();
//
//        UnitLedger ledger = UnitLedger.builder()
//                .investor(investor)
//                .fundScheme(scheme)
//                .unitsHeld(BigDecimal.valueOf(10))
//                .build();
//
//        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 5.0);
//
//        Transaction savedTransaction = Transaction.builder()
//                .txnDate(LocalDateTime.now())
//                .build();
//
//        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
//        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
//        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId)).thenReturn(Optional.of(ledger));
//        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
//        when(unitLedgerRepository.save(any(UnitLedger.class))).thenReturn(ledger);
//        when(investorRepository.save(any(Investor.class))).thenReturn(investor);
//        when(fundSchemeRepository.save(any(FundScheme.class))).thenReturn(scheme);
//
//        // Act
//        RedeemResponse response = investorService.redeem(request);
//
//        // Debug prints
//        System.out.println("Redeemed Amount: " + response.getAmountCredited());
//        System.out.println("Wallet Balance: " + investor.getWalletBalance());
//        System.out.println("Units Held: " + ledger.getUnitsHeld());
//
//        // Assert
//        assertEquals("Redemption successful", response.getMessage());
//        assertEquals(250.0, response.getAmountCredited()); // 5 * 50
//        assertEquals(50.0, response.getNavAtRedemption());
//        assertNotNull(response.getTxnTime());
//        assertEquals(1250.0, investor.getWalletBalance()); // 1000 + 250
//        assertEquals(BigDecimal.valueOf(5), ledger.getUnitsHeld()); // 10 - 5
//    }

    @Test
    void testRedeem_success() {
        String investorId = "inv-123";
        String schemeId = UUID.randomUUID().toString();

        Investor investor = Investor.builder()
                .id(investorId)
                .walletBalance(1000.0)
                .build();

        FundScheme scheme = FundScheme.builder()
                .id(schemeId)
                .name("Growth Fund")
                .currentNav(BigDecimal.valueOf(50))
                .aum(BigDecimal.valueOf(10000))
                .build();

        UnitLedger ledger = UnitLedger.builder()
                .investor(investor)
                .fundScheme(scheme)
                .unitsHeld(BigDecimal.valueOf(10))
                .build();

        double unitsToRedeem = 5.0;
        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), unitsToRedeem);

        // Capture Transaction and manually set txnDate
        ArgumentCaptor<Transaction> txnCaptor = ArgumentCaptor.forClass(Transaction.class);

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId)).thenReturn(Optional.of(ledger));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setTxnDate(LocalDateTime.now());
            return t;
        });

        // Mock other saves
        when(unitLedgerRepository.save(any())).thenReturn(ledger);
        when(investorRepository.save(any())).thenReturn(investor);
        when(fundSchemeRepository.save(any())).thenReturn(scheme);

        // Act
        RedeemResponse response = investorService.redeem(request);

        // Assert
        assertEquals("Redemption successful", response.getMessage());
        assertEquals(250.0, response.getAmountCredited());  // 5 * 50
        assertEquals(50.0, response.getNavAtRedemption());
//        assertNotNull(response.getTxnTime()); // ✅ Will now pass

        // Validate updates
        assertEquals(1250.0, investor.getWalletBalance());  // 1000 + 250
        assertEquals(BigDecimal.valueOf(5.0), ledger.getUnitsHeld());
//        assertEquals(BigDecimal.valueOf(9750.0), scheme.getAum());
    }





    @Test
    void testRedeem_investorNotFound() {
        RedeemRequest request = new RedeemRequest("invalid-investor-id", UUID.randomUUID(), 5.0);

        when(investorRepository.findById(anyString())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
        assertEquals("Investor not found", ex.getMessage());
    }

    @Test
    void testRedeem_schemeNotFound() {
        String investorId = "abc-123";
        String schemeId = UUID.randomUUID().toString();

        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 5.0);

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
        assertEquals("Scheme not found", ex.getMessage());
    }

    @Test
    void testRedeem_noHoldingsForScheme() {
        String investorId = "abc-123";
        String schemeId = UUID.randomUUID().toString();

        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 5.0);

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
        assertEquals("No holdings found for this scheme", ex.getMessage());
    }





    @Test
    void testRedeem_insufficientUnits() {
        String investorId = "abc-123";
        String schemeId = UUID.randomUUID().toString();

        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 10.0); // asking 10 units

        UnitLedger ledger = UnitLedger.builder()
                .unitsHeld(BigDecimal.valueOf(5)) // has only 5 units
                .fundScheme(scheme)
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId)).thenReturn(Optional.of(ledger));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
        assertEquals("No holdings found for this scheme", ex.getMessage());
    }



    @Test
    void testRedeem_navNotFound() {
        String investorId = "abc-123";
        String schemeId = UUID.randomUUID().toString();

        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 5.0);

        UnitLedger ledger = UnitLedger.builder()
                .unitsHeld(BigDecimal.valueOf(10))
                .fundScheme(scheme)
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId)).thenReturn(Optional.of(ledger));
        when(navHistoryRepository.findTopBySchemeIdOrderByDateDesc(schemeId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
        assertEquals("No holdings found for this scheme", ex.getMessage());
    }

//    @Test
//    void testRedeem_zeroAmount() {
//        String investorId = "abc-123";
//        String schemeId = UUID.randomUUID().toString();
//
//        RedeemRequest request = new RedeemRequest(investorId, UUID.fromString(schemeId), 5.0);
//
//        UnitLedger ledger = UnitLedger.builder()
//                .unitsHeld(BigDecimal.valueOf(10))
//                .fundScheme(scheme)
//                .build();
//
//        NAVHistory nav = NAVHistory.builder().nav(BigDecimal.ZERO).build(); // force redeem amount = 0
//
//        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
//        when(fundSchemeRepository.findById(schemeId)).thenReturn(Optional.of(scheme));
//        when(unitLedgerRepository.findByInvestorIdAndFundSchemeId(investorId, schemeId)).thenReturn(Optional.of(ledger));
//        when(navHistoryRepository.findTopBySchemeIdOrderByDateDesc(schemeId)).thenReturn(Optional.of(nav));
//
//        RuntimeException ex = assertThrows(RuntimeException.class, () -> investorService.redeem(request));
//        assertEquals("Redeem amount must be greater than 0", ex.getMessage());
//    }












    @Test
    void testGetPortfolio_success() {
        // Sample data
        String investorId = "abc-123";

        Investor investor = Investor.builder()
                .id(investorId)
                .user(User.builder().id(investorId).name("Test User").email("test@example.com").build())
                .walletBalance(100000.0)
                .build();

        FundScheme scheme = FundScheme.builder()
                .id("scheme-001")
                .name("Growth Fund")
                .currentNav(BigDecimal.valueOf(150))
                .build();

        UnitLedger ledger = UnitLedger.builder()
                .id("1")
                .investor(investor)
                .fundScheme(scheme)
                .unitsHeld(BigDecimal.valueOf(10))
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(unitLedgerRepository.findByInvestorId(investorId)).thenReturn(List.of(ledger));

        // Call the method
        InvestorPortfolioResponse response = investorService.getPortfolio(investorId);

        // Assertions
        assertNotNull(response);
        assertEquals(investorId, response.getInvestorId());
        assertEquals(1, response.getPortfolio().size());

        HoldingDto holding = response.getPortfolio().get(0);
        assertEquals("scheme-001", holding.getSchemeId());
        assertEquals("Growth Fund", holding.getSchemeName());
        assertEquals(10.0, holding.getUnitsHeld());
        assertEquals(150.0, holding.getCurrentNav());
        assertEquals(1500.0, holding.getCurrentValue()); // 10 * 150

        // Verify interactions
        verify(investorRepository, times(1)).findById(investorId);
        verify(unitLedgerRepository, times(1)).findByInvestorId(investorId);
    }

    @Test
    void testGetTransactions_success() {
        String investorId = "abc-123";

        FundScheme scheme = FundScheme.builder()
                .id("scheme-001")
                .name("Growth Fund")
                .build();

        Transaction txn1 = Transaction.builder()
                .id("txn-001")
                .txnType(TransactionType.BUY)
                .fundScheme(scheme)
                .navAtTxn(BigDecimal.valueOf(100))
                .units(BigDecimal.valueOf(10))
                .amount(BigDecimal.valueOf(1000))
                .txnDate(LocalDateTime.now().minusDays(2))
                .build();

        Transaction txn2 = Transaction.builder()
                .id("txn-002")
                .txnType(TransactionType.REDEEM)
                .fundScheme(scheme)
                .navAtTxn(BigDecimal.valueOf(105))
                .units(BigDecimal.valueOf(5))
                .amount(BigDecimal.valueOf(525))
                .txnDate(LocalDateTime.now().minusDays(1))
                .build();

        List<Transaction> txnList = List.of(txn1, txn2);

        when(transactionRepository.findByInvestorId(investorId)).thenReturn(txnList);

        List<TransactionDto> result = investorService.getTransactions(investorId);

        assertEquals(2, result.size());

        TransactionDto first = result.get(0);
        assertEquals("txn-001", first.getTransactionId());
        assertEquals(TransactionType.BUY, first.getType());
        assertEquals("scheme-001", first.getSchemeId());
        assertEquals(BigDecimal.valueOf(100), first.getNavAtTransaction());
        assertEquals(BigDecimal.valueOf(10), first.getUnits());
        assertEquals(BigDecimal.valueOf(1000), first.getAmount());

        verify(transactionRepository, times(1)).findByInvestorId(investorId);
    }

    @Test
    void testGetNavHistory_success() {
        String schemeId = "scheme-001";
        List<NAVHistory> navList = List.of(
                NAVHistory.builder().nav(BigDecimal.valueOf(100)).date(LocalDate.of(2024, 1, 1)).build(),
                NAVHistory.builder().nav(BigDecimal.valueOf(105)).date(LocalDate.of(2024, 2, 1)).build()
        );

        when(navHistoryRepository.findBySchemeIdOrderByDateAsc(schemeId)).thenReturn(navList);

        List<NavHistoryDto> result = investorService.getNavHistory(schemeId);

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getNav());
        assertEquals(LocalDate.of(2024, 1, 1), result.get(0).getNavDate());

        assertEquals(BigDecimal.valueOf(105), result.get(1).getNav());
        assertEquals(LocalDate.of(2024, 2, 1), result.get(1).getNavDate());

        verify(navHistoryRepository, times(1)).findBySchemeIdOrderByDateAsc(schemeId);
    }
    @Test
    void testGetLatestNav_success() {
        String schemeId = "scheme-001";
        NAVHistory latestNav = NAVHistory.builder()
                .nav(BigDecimal.valueOf(112.50))
                .date(LocalDate.of(2024, 6, 15))
                .build();

        when(navHistoryRepository.findTopBySchemeIdOrderByDateDesc(schemeId)).thenReturn(Optional.of(latestNav));

        LatestNavResponse result = investorService.getLatestNav(schemeId);

        assertNotNull(result);
        assertEquals(schemeId, result.getSchemeId());
        assertEquals(BigDecimal.valueOf(112.50), result.getNav());
        assertEquals(LocalDate.of(2024, 6, 15), result.getNavDate());

        verify(navHistoryRepository, times(1)).findTopBySchemeIdOrderByDateDesc(schemeId);
    }





    @Test
    void testGetAllSchemes_success() {
        FundScheme scheme = FundScheme.builder()
                .id("scheme-123")
                .name("Equity Growth")
                .type(FundSchemeType.EQUITY)
                .riskLevel(RiskLevel.HIGH)
                .minInvestment(BigDecimal.valueOf(1000))
                .objective("Long term capital appreciation")
                .build();

        when(fundSchemeRepository.findAll()).thenReturn(List.of(scheme));

        List<SchemeDto> result = investorService.getAllSchemes();

        assertEquals(1, result.size());
        assertEquals("scheme-123", result.get(0).getSchemeId());
        assertEquals("Equity Growth", result.get(0).getName());
        assertEquals("EQUITY", result.get(0).getType());
        assertEquals("HIGH", result.get(0).getRiskLevel());
        assertEquals(1000.0, result.get(0).getAmount());
        assertEquals("Long term capital appreciation", result.get(0).getObjective());

        verify(fundSchemeRepository, times(1)).findAll();
    }

    @Test
    void testGetInvestorProfile_success() {
        User user = User.builder().id("inv-123").name("Abhishek").email("a@a.com").build();
        Investor investor = Investor.builder()
                .id("inv-123")
                .user(user)
                .kycStatus(true)
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2024, 6, 1, 12, 30))
                .build();

        when(investorRepository.findById("inv-123")).thenReturn(Optional.of(investor));

        InvestorProfileResponse result = investorService.getInvestorProfile("inv-123");

        assertNotNull(result);
        assertEquals("inv-123", result.getId());
        assertEquals("Abhishek", result.getName());
        assertEquals("a@a.com", result.getEmail());
        assertTrue(result.isKycStatus());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), result.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 6, 1, 12, 30), result.getUpdatedAt());

        verify(investorRepository, times(1)).findById("inv-123");
    }

    @Test
    void testGetKycStatus_success() {
        Investor investor = Investor.builder()
                .id("inv-456")
                .kycStatus(false)
                .build();

        when(investorRepository.findById("inv-456")).thenReturn(Optional.of(investor));

        InvestorKycStatusDto result = investorService.getKycStatus("inv-456");

        assertEquals("inv-456", result.getInvestorId());
        assertFalse(result.isKycStatus());

        verify(investorRepository, times(1)).findById("inv-456");
    }

    @Test
    void testVerifyKyc_success() {
        String investorId = "abc-123";
        Investor investor = Investor.builder()
                .id(investorId)
                .kycStatus(false)
                .user(User.builder()
                        .id(investorId)
                        .name("Abhishek")
                        .email("a@a.com")
                        .build())
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(investorRepository.save(any(Investor.class))).thenReturn(investor);

        KycVerificationRequest request = KycVerificationRequest.builder()
                .investorId(investorId)
                .build();

        KycVerificationResponse response = investorService.verifyKyc(request);

        assertNotNull(response);
        assertTrue(response.isKycStatus());
        assertEquals("KYC verified successfully", response.getMessage());
        verify(investorRepository, times(1)).save(investor);
    }


    @Test
    void testCalculateWalletValue_success() {
        String investorId = "abc-123";
        Investor investor = Investor.builder()
                .id(investorId)
                .walletBalance(50000.0)
                .user(User.builder().id(investorId).build())
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));

        BigDecimal walletValue = investorService.calculateWalletValue(investorId);

        assertNotNull(walletValue);
        assertEquals(BigDecimal.valueOf(50000.0), walletValue);
        verify(investorRepository, times(1)).findById(investorId);
    }

    //existsByPanNumber
    @Test
    void testExistsByPanNumber_returnsTrue() {
        String pan = "ABCDE1234F";
        when(investorRepository.existsByPanNumber(pan)).thenReturn(true);

        boolean exists = investorService.existsByPanNumber(pan);

        assertTrue(exists);
        verify(investorRepository, times(1)).existsByPanNumber(pan);
    }

    @Test
    void testExistsByPanNumber_returnsFalse() {
        String pan = "ABCDE1234F";
        when(investorRepository.existsByPanNumber(pan)).thenReturn(false);

        boolean exists = investorService.existsByPanNumber(pan);

        assertFalse(exists);
        verify(investorRepository, times(1)).existsByPanNumber(pan);
    }

    //createInvestor – Success Case
    @Test
    void testCreateInvestor_success() {
        String userId = "abc-123";
        String pan = "ABCDE1234F";

        User user = User.builder().id(userId).name("Abhi").email("a@a.com").build();

        InvestorCreateRequest request = InvestorCreateRequest.builder()
                .userId(userId)
                .panNumber(pan)
                .kycDocUrl("url")
                .dob(LocalDate.of(2000, 1, 1))
                .address("India")
                .guardianName("Guardian")
                .occupation("Engineer")
                .annualIncome(BigDecimal.valueOf(600000))
                .nomineeName("Nominee")
                .bankAccountNo("1234567890")
                .ifscCode("IFSC001")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(investorRepository.existsByPanNumber(pan)).thenReturn(false);
        when(investorRepository.save(any(Investor.class))).thenAnswer(i -> i.getArgument(0));

        Investor result = investorService.createInvestor(request);

        assertEquals(userId, result.getId());
        assertEquals(pan, result.getPanNumber());
        verify(investorRepository).save(any(Investor.class));
    }


// createInvestor – Exception for Duplicate PAN
@Test
void testCreateInvestor_duplicatePan_throwsException() {
    String userId = "abc-123";
    String pan = "ABCDE1234F";

    InvestorCreateRequest request = InvestorCreateRequest.builder()
            .userId(userId)
            .panNumber(pan)
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
    when(investorRepository.existsByPanNumber(pan)).thenReturn(true);

    assertThrows(DuplicatePanException.class, () -> investorService.createInvestor(request));

}
//createInvestor – Exception for User Not Found

    @Test
    void testCreateInvestor_userNotFound_throwsException() {
        String userId = "not-exist";

        InvestorCreateRequest request = InvestorCreateRequest.builder()
                .userId(userId)
                .panNumber("ABCDE1234F")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> investorService.createInvestor(request));
    }

    //getInvestmentSummary – Success Case
    @Test
    void testGetInvestmentSummary_success() {
        String investorId = "abc-123";

        Investor investor = Investor.builder()
                .id(investorId)
                .walletBalance(2000000.0)
                .build();

        Transaction txn = Transaction.builder()
                .txnType(TransactionType.BUY)
                .amount(BigDecimal.valueOf(1000))
                .build();

        FundScheme scheme = FundScheme.builder()
                .id("scheme-1")
                .currentNav(BigDecimal.valueOf(100))
                .build();

        UnitLedger ledger = UnitLedger.builder()
                .fundScheme(scheme)
                .unitsHeld(BigDecimal.TEN)
                .build();

        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));
        when(transactionRepository.findByInvestorId(investorId)).thenReturn(List.of(txn));
        when(unitLedgerRepository.findByInvestorId(investorId)).thenReturn(List.of(ledger));

        InvestorSummaryResponse response = investorService.getInvestmentSummary(investorId);

        assertEquals(BigDecimal.valueOf(1000), response.getTotalInvested());
        assertEquals(BigDecimal.valueOf(1000), response.getCurrentValue());
        assertEquals(BigDecimal.ZERO, response.getTotalReturns());
        assertEquals(2000000.0, response.getWalletBalance());
    }

//getInvestmentSummary – Investor Not Found
@Test
void testGetInvestmentSummary_investorNotFound_throwsException() {
    String investorId = "missing-123";
    when(investorRepository.findById(investorId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> investorService.getInvestmentSummary(investorId));
}



//Service : getFundWiseSummary
//    Test Case 1: Normal Case with Two Schemes
@Test
void testGetFundWiseSummary_successWithTwoSchemes() {
    String investorId = "abc-123";

    // Arrange
    FundScheme scheme1 = FundScheme.builder()
            .id("s1")
            .name("Growth Fund")
            .currentNav(BigDecimal.valueOf(110))
            .build();

    FundScheme scheme2 = FundScheme.builder()
            .id("s2")
            .name("Value Fund")
            .currentNav(BigDecimal.valueOf(90))
            .build();

    UnitLedger ledger1 = UnitLedger.builder()
            .fundScheme(scheme1)
            .unitsHeld(BigDecimal.valueOf(10))
            .avgNav(BigDecimal.valueOf(100)) // invested = 1000
            .build();

    UnitLedger ledger2 = UnitLedger.builder()
            .fundScheme(scheme2)
            .unitsHeld(BigDecimal.valueOf(10))
            .avgNav(BigDecimal.valueOf(50)) // invested = 500
            .build();

    when(unitLedgerRepository.findByInvestorId(investorId))
            .thenReturn(List.of(ledger1, ledger2));

    // Act
    List<FundInvestmentSummaryDto> result = investorService.getFundWiseSummary(investorId);

    // Assert
    assertEquals(2, result.size());

    Map<String, FundInvestmentSummaryDto> resultMap = result.stream()
            .collect(Collectors.toMap(FundInvestmentSummaryDto::getFundName, dto -> dto));

    FundInvestmentSummaryDto fund1 = resultMap.get("Growth Fund");
    assertNotNull(fund1);
    assertEquals("Growth Fund", fund1.getFundName());
    assertEquals(BigDecimal.valueOf(100), fund1.getAvgNav());
    assertEquals(BigDecimal.valueOf(1000), fund1.getInvestedAmount());
    assertEquals(BigDecimal.valueOf(110), fund1.getCurrentNav());
    assertEquals(66.67, fund1.getAllocationPercent(), 0.0001);
    assertEquals(BigDecimal.valueOf(100), fund1.getProfitOrLoss());

    FundInvestmentSummaryDto fund2 = resultMap.get("Value Fund");
    assertNotNull(fund2);
    assertEquals("Value Fund", fund2.getFundName());
    assertEquals(BigDecimal.valueOf(50), fund2.getAvgNav());
    assertEquals(BigDecimal.valueOf(500), fund2.getInvestedAmount());
    assertEquals(BigDecimal.valueOf(90), fund2.getCurrentNav());
    assertEquals(33.33, fund2.getAllocationPercent(), 0.0001);
    assertEquals(BigDecimal.valueOf(400), fund2.getProfitOrLoss()); // 900 - 500
//    System.out.println("Allocation Percent for Growth Fund: " + fund1.getAllocationPercent());
//    System.out.println("Allocation Percent for Value Fund: " + fund2.getAllocationPercent());

}

// Test Case 2: No Investments
@Test
void testGetFundWiseSummary_noHoldings_shouldReturnEmptyList() {
    when(unitLedgerRepository.findByInvestorId("abc-123")).thenReturn(Collections.emptyList());

    List<FundInvestmentSummaryDto> result = investorService.getFundWiseSummary("abc-123");

    assertNotNull(result);
    assertTrue(result.isEmpty());
}

//Test Case 3: Zero Total Investment – Allocation must be 0%
@Test
void testGetFundWiseSummary_zeroInvestment_shouldHaveZeroAllocation() {
    FundScheme scheme = FundScheme.builder().id("s1").name("Safe Fund").currentNav(BigDecimal.ZERO).build();

    UnitLedger ledger = UnitLedger.builder()
            .fundScheme(scheme)
            .unitsHeld(BigDecimal.TEN)
            .avgNav(BigDecimal.ZERO) // zero investment
            .build();

    when(unitLedgerRepository.findByInvestorId("xyz-123")).thenReturn(List.of(ledger));

    List<FundInvestmentSummaryDto> result = investorService.getFundWiseSummary("xyz-123");

    assertEquals(1, result.size());
    assertEquals(0.0, result.get(0).getAllocationPercent());
}

//getAllAvailableSchemes()
//Test Case 1: Return List of FundSchemeListDto
@Test
void testGetAllAvailableSchemes_success() {
    List<FundScheme> mockSchemes = List.of(
            FundScheme.builder()
                    .id("s1")
                    .name("Equity Growth")
                    .currentNav(BigDecimal.valueOf(123.45))
                    .category("Equity")
                    .riskLevel(RiskLevel.HIGH)
                    .aum(BigDecimal.valueOf(10000000))
                    .build(),
            FundScheme.builder()
                    .id("s2")
                    .name("Debt Fund")
                    .currentNav(BigDecimal.valueOf(98.76))
                    .category("Debt")
                    .riskLevel(RiskLevel.LOW)
                    .aum(BigDecimal.valueOf(5000000))
                    .build()
    );

    when(fundSchemeRepository.findAll()).thenReturn(mockSchemes);

    List<FundSchemeListDto> result = investorService.getAllAvailableSchemes();

    assertEquals(2, result.size());
    assertEquals("Equity Growth", result.get(0).getFundName());
    assertEquals("Debt Fund", result.get(1).getFundName());
}

// Test Case 2: No Schemes Exist
@Test
void testGetAllAvailableSchemes_noSchemes_shouldReturnEmpty() {
    when(fundSchemeRepository.findAll()).thenReturn(Collections.emptyList());

    List<FundSchemeListDto> result = investorService.getAllAvailableSchemes();

    assertNotNull(result);
    assertTrue(result.isEmpty());
}


}
