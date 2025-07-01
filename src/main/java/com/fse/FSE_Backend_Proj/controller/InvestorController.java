
package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.model.Investor;
import com.fse.FSE_Backend_Proj.service.InvestorService;
import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;

    @PostMapping("/invest")
    public ResponseEntity<InvestmentResponse> invest(@RequestBody InvestmentRequest request) {
        return ResponseEntity.ok(investorService.invest(request));
    }
    @GetMapping("/wallet-value/{investorId}")
    public ResponseEntity<WalletValueResponse> getWalletValue(@PathVariable String investorId) {
        BigDecimal walletValue = investorService.calculateWalletValue(investorId);
        return ResponseEntity.ok(new WalletValueResponse(walletValue));
    }

    @PostMapping("/create")
    public ResponseEntity<Investor> createInvestor(@RequestBody InvestorCreateRequest request) {
        Investor created = investorService.createInvestor(request);
        return ResponseEntity.ok(created);
    }





    @PostMapping("/redeem")
    public ResponseEntity<RedeemResponse> redeem(@RequestBody RedeemRequest request) {
        return ResponseEntity.ok(investorService.redeem(request));
    }

    @GetMapping("/portfolio/{investorId}")
    public ResponseEntity<InvestorPortfolioResponse> getPortfolio(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getPortfolio(investorId));
    }

    @GetMapping("/transactions/{investorId}")
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable String investorId) {
        return ResponseEntity.ok(investorService.getTransactions(investorId));
    }

    @GetMapping("/nav-history/{schemeId}")
    public ResponseEntity<List<NavHistoryDto>> getNavHistory(@PathVariable String schemeId) {
        return ResponseEntity.ok(investorService.getNavHistory(schemeId));
    }

    @GetMapping("/schemes")
    public ResponseEntity<List<SchemeDto>> getAllSchemes() {
        return ResponseEntity.ok(investorService.getAllSchemes());
    }

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

    @PostMapping("/kyc/verify")
    public ResponseEntity<KycVerificationResponse> verifyKyc(@RequestBody KycVerificationRequest request) {
        return ResponseEntity.ok(investorService.verifyKyc(request));
    }
}
