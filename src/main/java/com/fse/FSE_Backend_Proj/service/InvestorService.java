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

    List<TransactionDto> getTransactionsByScheme(String investorId, String schemeId);

    List<SchemeDto> getAllSchemes();

    InvestorProfileResponse getInvestorProfile(String investorId);

    LatestNavResponse getLatestNav(String schemeId);

    InvestorKycStatusDto getKycStatus(String investorId);

    KycVerificationResponse verifyKyc(KycVerificationRequest request);


    boolean existsByPanNumber(String panNumber);


    Investor createInvestor(InvestorCreateRequest request);

    InvestorSummaryResponse getInvestmentSummary(String investorId);

    List<FundInvestmentSummaryDto> getFundWiseSummary(String investorId);

    List<FundSchemeListDto> getAllAvailableSchemes();
}


