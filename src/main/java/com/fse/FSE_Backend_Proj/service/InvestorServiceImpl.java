package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.investorDto.*;
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

        NAVHistory latestNav = navHistoryRepository.findTopBySchemeIdOrderByDateDesc
                        (scheme.getId())
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

        BigDecimal currentBalance = BigDecimal.valueOf(investor.getWalletBalance());
        BigDecimal updatedBalance = currentBalance.add(redeemAmount);
        investor.setWalletBalance(updatedBalance.doubleValue());
        investorRepository.save(investor);

        return new RedeemResponse(
                "Redemption successful",
                redeemAmount.doubleValue(), // amount credited
                nav.doubleValue(),          // nav at redemption
                txn.getTxnDate()            // transaction time
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

    @Override
    public Investor createInvestor(InvestorCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

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






}
