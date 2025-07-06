package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.investorDto.*;
import com.fse.FSE_Backend_Proj.exception.DuplicatePanException;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.model.enums.TransactionType;
import com.fse.FSE_Backend_Proj.repository.FundSchemeRepository;
import com.fse.FSE_Backend_Proj.repository.InvestorRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestorServiceImpl implements InvestorService {

    private final InvestorRepository investorRepository;
    private final FundSchemeRepository fundSchemeRepository;
    private final com.fse.FSE_Backend_Proj.repository.TransactionRepository transactionRepository;
    private final com.fse.FSE_Backend_Proj.repository.UnitLedgerRepository unitLedgerRepository;
    private final com.fse.FSE_Backend_Proj.repository.NAVHistoryRepository navHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public InvestmentResponse invest(InvestmentRequest request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new RuntimeException("Investor not found"));

//        FundScheme scheme = fundSchemeRepository.findById(String.valueOf(request.getSchemeId()))
//                .orElseThrow(() -> new RuntimeException("Scheme not found"));
        FundScheme scheme = fundSchemeRepository.findById(String.valueOf(UUID.fromString(request.getSchemeId())))
                .orElseThrow(() -> new RuntimeException("Scheme not found"));

        NAVHistory latestNav = navHistoryRepository.findTopBySchemeIdOrderByDateDesc
                        (scheme.getId())
                .orElseThrow(() -> new RuntimeException("NAV not found"));

        BigDecimal nav = latestNav.getNav();
        BigDecimal amount = request.getAmount();
        BigDecimal units = amount.divide(nav, 4, RoundingMode.HALF_UP);

        Transaction txn = Transaction.builder()
                .investor(investor)
                .fundScheme(scheme)
                .txnType(TransactionType.BUY)
                .amount(amount)
                .units(units)
                .navAtTxn(nav)
                .build();

        transactionRepository.save(txn);

        UnitLedger ledger = unitLedgerRepository.findByInvestorIdAndFundSchemeId(investor.getId(), scheme.getId())
                .orElse(UnitLedger.builder()
                        .investor(investor)
                        .fundScheme(scheme)
                        .unitsHeld(BigDecimal.ZERO)
                        .avgNav(nav)
                        .lastUpdated(LocalDateTime.now())
                        .build());

        ledger.setUnitsHeld(ledger.getUnitsHeld().add(units));
        ledger.setAvgNav(nav);
        ledger.setLastUpdated(LocalDateTime.now());

        unitLedgerRepository.save(ledger);


        scheme.setAum(scheme.getAum().add(amount)); //add fund_scheme aum method
        fundSchemeRepository.save(scheme);

        BigDecimal currentBalance = BigDecimal.valueOf(investor.getWalletBalance());
        BigDecimal updatedBalance = currentBalance.subtract(amount);
        investor.setWalletBalance(updatedBalance.doubleValue());
        investorRepository.save(investor);

        return new InvestmentResponse("Investment successful", units.doubleValue(), nav.doubleValue(), txn.getTxnDate());
    }

    @Override
    public RedeemResponse redeem(RedeemRequest request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        FundScheme scheme = fundSchemeRepository.findById(String.valueOf(UUID.fromString(String.valueOf(request.getSchemeId()))))
                .orElseThrow(() -> new RuntimeException("Scheme not found"));

        UnitLedger ledger = unitLedgerRepository.findByInvestorIdAndFundSchemeId(investor.getId(), scheme.getId())
                .orElseThrow(() -> new RuntimeException("No holdings found for this scheme"));

        BigDecimal redeemUnits = BigDecimal.valueOf(request.getUnitsToRedeem());
        if (ledger.getUnitsHeld().compareTo(redeemUnits) < 0) {
            throw new RuntimeException("Insufficient units to redeem");
        }

        NAVHistory latestNav = navHistoryRepository.findTopBySchemeIdOrderByDateDesc(scheme.getId())
                .orElseThrow(() -> new RuntimeException("NAV not found"));

        BigDecimal nav = latestNav.getNav();
        BigDecimal redeemAmount = redeemUnits.multiply(nav).setScale(2, RoundingMode.HALF_UP);
        if (redeemAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Redeem amount must be greater than 0");
        }

        Transaction txn = Transaction.builder()
                .investor(investor)
                .fundScheme(scheme)
                .txnType(TransactionType.REDEEM)
                .amount(redeemAmount)
                .units(redeemUnits)
                .navAtTxn(nav)
                .build();

        transactionRepository.save(txn);

        ledger.setUnitsHeld(ledger.getUnitsHeld().subtract(redeemUnits));
        ledger.setLastUpdated(LocalDateTime.now());
        unitLedgerRepository.save(ledger);

        // 🟢 Update Investor Wallet
        BigDecimal currentBalance = BigDecimal.valueOf(investor.getWalletBalance());
        BigDecimal updatedBalance = currentBalance.add(redeemAmount);
        investor.setWalletBalance(updatedBalance.doubleValue());
        investorRepository.save(investor);

        // 🟡 Update AUM in FundScheme
        BigDecimal currentAum = scheme.getAum() != null ? scheme.getAum() : BigDecimal.ZERO;
        BigDecimal updatedAum = currentAum.subtract(redeemAmount).max(BigDecimal.ZERO);
        scheme.setAum(updatedAum);
        fundSchemeRepository.save(scheme);

        return new RedeemResponse(
                "Redemption successful",
                redeemAmount.doubleValue(),
                nav.doubleValue(),
                txn.getTxnDate()
        );
    }



    @Override
    public InvestorPortfolioResponse getPortfolio(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        List<UnitLedger> ledgers = unitLedgerRepository.findByInvestorId(investorId);

        List<HoldingDto> portfolioEntries = ledgers.stream()
                .map(ledger -> {
                    FundScheme scheme = ledger.getFundScheme();
                    BigDecimal unitsHeld = ledger.getUnitsHeld();
                    BigDecimal latestNav = scheme.getCurrentNav(); // assuming this field is updated regularly
                    BigDecimal currentValue = unitsHeld.multiply(latestNav);

                    return new HoldingDto(
                            scheme.getId(),
                            scheme.getName(),
                            unitsHeld.doubleValue(),
                            latestNav.doubleValue(),
                            currentValue.doubleValue()
                    );
                }).collect(Collectors.toList());

        return new InvestorPortfolioResponse(investorId, portfolioEntries);
    }


    @Override
    public List<TransactionDto> getTransactions(String investorId) {
        return transactionRepository.findByInvestorId(investorId).stream()
                .map(txn -> new TransactionDto(
                        txn.getId(),
                        txn.getTxnType(),
                        txn.getFundScheme().getId(),
                        txn.getNavAtTxn(),
                        txn.getUnits(),
                        txn.getAmount(),
                        txn.getTxnDate()
                )).collect(Collectors.toList());
    }

    @Override
    public List<NavHistoryDto> getNavHistory(String schemeId) {
        return navHistoryRepository.findBySchemeIdOrderByDateAsc(schemeId).stream()
                .map(n -> new NavHistoryDto(n.getNav(), n.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public LatestNavResponse getLatestNav(String schemeId) {
        NAVHistory latest = navHistoryRepository.findTopBySchemeIdOrderByDateDesc
                        (schemeId)
                .orElseThrow(() -> new RuntimeException("NAV not found"));

        return new LatestNavResponse(schemeId, latest.getNav(), latest.getDate());
    }

    @Override
    public List<SchemeDto> getAllSchemes() {
        return fundSchemeRepository.findAll().stream()
                .map(scheme -> new SchemeDto(
                        scheme.getId(),
                        scheme.getName(),
                        scheme.getType().name(),
                        scheme.getRiskLevel().name(),
                        scheme.getMinInvestment().doubleValue(),
                        scheme.getObjective()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public InvestorProfileResponse getInvestorProfile(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        return new InvestorProfileResponse(
                investor.getId(),
                investor.getUser().getName(),
                investor.getUser().getEmail(),
                investor.isKycStatus(),
                investor.getCreatedAt(),
                investor.getUpdatedAt()
        );
    }

    @Override
    public InvestorKycStatusDto getKycStatus(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        return new InvestorKycStatusDto(investor.getId(), investor.isKycStatus());
    }

    @Override
    public KycVerificationResponse verifyKyc(KycVerificationRequest request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        investor.setKycStatus(true);
        investorRepository.save(investor);

        return new KycVerificationResponse("KYC verified successfully" , true);
    }

    @Override
    public BigDecimal calculateWalletValue(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));
        return BigDecimal.valueOf(investor.getWalletBalance());
    }
//    @Override
//    public boolean getInvestorById(String investorId) {
//        System.out.println("API called inside getInvestorById");
//        return investorRepository.existsById(investorId);
//    }


    @Override
    public boolean existsByPanNumber(String panNumber) {
        return investorRepository.existsByPanNumber(panNumber);
    }


    @Override
    public Investor createInvestor(InvestorCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (investorRepository.existsByPanNumber(request.getPanNumber())) {
            throw new DuplicatePanException("PAN number already exists.");
        }



        Investor investor = Investor.builder()
                .id(user.getId()) // Map userId
                .user(user)
                .kycStatus(false)
                .kycDocUrl(request.getKycDocUrl())
                .dob(request.getDob())
                .panNumber(request.getPanNumber())
                .address(request.getAddress())
                .guardianName(request.getGuardianName())
                .occupation(request.getOccupation())
                .annualIncome(request.getAnnualIncome())
                .nomineeName(request.getNomineeName())
                .bankAccountNo(request.getBankAccountNo())
                .ifscCode(request.getIfscCode())
                .walletBalance(2000000.0) // Default or skip if PrePersist works
                .build();

        return investorRepository.save(investor);
    }


    @Override
    public InvestorSummaryResponse getInvestmentSummary(String investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        // Total invested = sum of all BUY transactions
        BigDecimal totalInvested = transactionRepository.findByInvestorId(investorId).stream()
                .filter(txn -> txn.getTxnType() == TransactionType.BUY)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Current value = sum of (unitsHeld * latest NAV) for all holdings
        BigDecimal currentValue = unitLedgerRepository.findByInvestorId(investorId).stream()
                .map(ledger -> {
                    BigDecimal latestNav = ledger.getFundScheme().getCurrentNav();
                    return ledger.getUnitsHeld().multiply(latestNav);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Returns = currentValue - totalInvested
        BigDecimal returns = currentValue.subtract(totalInvested);

        return new InvestorSummaryResponse(
                totalInvested,
                currentValue,
                returns,
                investor.getWalletBalance()
        );
    }

    @Override
    public List<FundInvestmentSummaryDto> getFundWiseSummary(String investorId) {
        List<UnitLedger> ledgers = unitLedgerRepository.findByInvestorId(investorId);

        // Total amount investor has invested across all schemes
        BigDecimal totalInvested = ledgers.stream()
                .map(ledger -> ledger.getUnitsHeld().multiply(ledger.getAvgNav()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ledgers.stream().map(ledger -> {
            FundScheme scheme = ledger.getFundScheme();
            BigDecimal unitsHeld = ledger.getUnitsHeld();
            BigDecimal avgNav = ledger.getAvgNav();
            BigDecimal investedAmount = unitsHeld.multiply(avgNav);
            BigDecimal currentNav = scheme.getCurrentNav();
            BigDecimal currentValue = unitsHeld.multiply(currentNav);
            BigDecimal profitOrLoss = currentValue.subtract(investedAmount);

            double allocationPercent = totalInvested.compareTo(BigDecimal.ZERO) > 0
                    ? investedAmount.divide(totalInvested, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue()
                    : 0.0;

            return new FundInvestmentSummaryDto(
                    scheme.getName(),
                    avgNav,
                    investedAmount,
                    currentNav,
                    allocationPercent,
                    profitOrLoss,
                    scheme.getId()
            );
        }).collect(Collectors.toList());
    }


    @Override
    public List<FundSchemeListDto> getAllAvailableSchemes() {
        List<FundScheme> schemes = fundSchemeRepository.findAll();

        return schemes.stream().map(s -> new FundSchemeListDto(
                s.getId(),
                s.getName(),
                s.getCurrentNav(),
                s.getCategory(),
                s.getRiskLevel().name(),
                s.getAum()
        )).collect(Collectors.toList());
    }





}
