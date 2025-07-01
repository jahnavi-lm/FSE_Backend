package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import com.fse.FSE_Backend_Proj.model.Investor;

import java.math.BigDecimal;
import java.util.List;

public interface InvestorService {

    InvestmentResponse invest(InvestmentRequest request);
    BigDecimal calculateWalletValue(String investorId);
    RedeemResponse redeem(RedeemRequest request);

    InvestorPortfolioResponse getPortfolio(String investorId);

    List<TransactionDto> getTransactions(String investorId);

    List<NavHistoryDto> getNavHistory(String schemeId);

    List<SchemeDto> getAllSchemes();

    InvestorProfileResponse getInvestorProfile(String investorId);

    LatestNavResponse getLatestNav(String schemeId);

    InvestorKycStatusDto getKycStatus(String investorId);

    KycVerificationResponse verifyKyc(KycVerificationRequest request);
    Investor createInvestor(InvestorCreateRequest request);

}

