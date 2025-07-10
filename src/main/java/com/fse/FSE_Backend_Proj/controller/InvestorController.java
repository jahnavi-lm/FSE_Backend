package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import com.fse.FSE_Backend_Proj.model.Investor;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.repository.InvestorRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.service.InvestorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;
    private final UserRepository userRepository;  // ✅ Inject UserRepository
    private final InvestorRepository investorRepository;

    @PostMapping("/create")
    public ResponseEntity<Investor> createInvestor(@RequestBody InvestorCreateRequest request) {
        System.out.println("inside create");
        Investor created = investorService.createInvestor(request);
        return ResponseEntity.ok(created);
    }


    @PostMapping("/invest")
    public ResponseEntity<InvestmentResponse> invest(@RequestBody InvestmentRequest request) {
        return ResponseEntity.ok(investorService.invest(request));
    }

    @PostMapping("/redeem")
    public ResponseEntity<RedeemResponse> redeem(@RequestBody RedeemRequest request) {
        return ResponseEntity.ok(investorService.redeem(request));
    }

    @GetMapping("/wallet-value/{investorId}")
    public ResponseEntity<WalletValueResponse> getWalletValue(@PathVariable String investorId) {
        BigDecimal walletValue = investorService.calculateWalletValue(investorId);
        return ResponseEntity.ok(new WalletValueResponse(walletValue));
    }

    @GetMapping("/portfolio/{investorId}")
    public ResponseEntity<InvestorPortfolioResponse> getPortfolio(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getPortfolio(investorId));
    }

    @GetMapping("/transactions/{investorId}")
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getTransactions(investorId));
    }

    @GetMapping("/transactions/{investorId}/scheme/{schemeId}")
    public ResponseEntity<List<TransactionDto>> getInvestorTransactionsByScheme(
            @PathVariable String investorId,
            @PathVariable String schemeId) {
        List<TransactionDto> transactions = investorService.getTransactionsByScheme(investorId, schemeId);
        return ResponseEntity.ok(transactions);
    }



    @GetMapping("/nav-history/{schemeId}")
    public ResponseEntity<List<NavHistoryDto>> getNavHistory(@PathVariable String schemeId) {
        return ResponseEntity.ok(investorService.getNavHistory(schemeId));
    }

//    @GetMapping("/schemes")
//    public ResponseEntity<List<SchemeDto>> getAllSchemes() {
//        return ResponseEntity.ok(investorService.getAllSchemes());
//    }

    @GetMapping("/profile/{investorId}")
    public ResponseEntity<InvestorProfileResponse> getProfile(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getInvestorProfile(investorId));
    }

    @GetMapping("/nav-latest/{schemeId}")
    public ResponseEntity<LatestNavResponse> getLatestNav(@PathVariable String schemeId) {
        return ResponseEntity.ok(investorService.getLatestNav(schemeId));
    }

    @GetMapping("/kyc-status/{investorId}")
    public ResponseEntity<InvestorKycStatusDto> getKycStatus(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getKycStatus(investorId));
    }


    @GetMapping("/exists/{investorId}")
    public ResponseEntity<Boolean> checkInvestorExists(@PathVariable UUID investorId){
        boolean exists = investorRepository.existsById(String.valueOf(investorId));
        return ResponseEntity.ok(exists);

    }





    @PostMapping("/kyc/verify")
    public ResponseEntity<KycVerificationResponse> verifyKyc(@RequestBody KycVerificationRequest request) {
        return ResponseEntity.ok(investorService.verifyKyc(request));
    }


    //totalInvested , currentValue , totalReturns , walletBalance
    @GetMapping("/summary/{investorId}")
    public ResponseEntity<InvestorSummaryResponse> getInvestmentSummary(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getInvestmentSummary(investorId));
    }

    @GetMapping("/fund-summary/{investorId}")
    public ResponseEntity<List<FundInvestmentSummaryDto>> getFundSummary(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getFundWiseSummary(investorId));
    }

    @GetMapping("/available-schemes")
    public ResponseEntity<List<FundSchemeListDto>> getAllSchemes() {
        return ResponseEntity.ok(investorService.getAllAvailableSchemes());
    }







}
