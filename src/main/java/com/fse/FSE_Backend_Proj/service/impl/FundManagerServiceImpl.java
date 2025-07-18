package com.fse.FSE_Backend_Proj.service.impl;

import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.TotalAmount;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.CompanyInvestmentDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyAndBacktestCountDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.repository.*;
import com.fse.FSE_Backend_Proj.service.FundManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundManagerServiceImpl implements FundManagerService {

    private final FundManagerRepository fundManagerRepository;
    private final UserRepository userRepository;
    private final AMCRepository amcRepository;
    private final FundSchemeRepository fundSchemeRepository;
    private final CompanyRepository companyRepository;
    private final CompanyInvestmentRepository companyInvestmentRepository;
    private final FundManagerTransactionRepository fundManagerTransactionRepository;
    private final StrategyRepository strategyRepository;
    private final BacktestResultRepository backtestResultRepository;

    @Override
    public FundManagerResponseDto create(FundManagerRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (fundManagerRepository.existsById(user.getId())) {
            throw new IllegalStateException("Fund Manager already exists for this user");
        }

        FundManager fm = FundManager.builder()
                .user(user)
                .employeeCode(dto.getEmployeeCode())
                .qualification(dto.getQualification())
                .experienceYears(dto.getExperienceYears())
                .bio(dto.getBio())
                .build();

        fm = fundManagerRepository.save(fm);
        return toDto(fm);
    }

    @Override
    public FundManagerResponseDto getById(String id) {
        FundManager fm = fundManagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found"));
        return toDto(fm);
    }

    @Override
    public List<FundManagerResponseDto> getAll() {
        return fundManagerRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FundManagerResponseDto update(String id, FundManagerRequestDto dto) {
        FundManager fm = fundManagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found"));

        fm.setEmployeeCode(dto.getEmployeeCode());
        fm.setQualification(dto.getQualification());
        fm.setExperienceYears(dto.getExperienceYears());
        fm.setBio(dto.getBio());

        return toDto(fundManagerRepository.save(fm));
    }

    @Override
    public void delete(String id) {
        FundManager fm = fundManagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found"));
        fundManagerRepository.delete(fm);
    }

    private FundManagerResponseDto toDto(FundManager fm) {
        return FundManagerResponseDto.builder()
                .id(fm.getId())
                .employeeCode(fm.getEmployeeCode())
                .qualification(fm.getQualification())
                .experienceYears(fm.getExperienceYears())
                .bio(fm.getBio())
                .build();
    }

    @Override
    public List<FundSchemeResponseDto> getSchemesByFundManagerId(String id) {
        fundManagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found"));
        List<FundScheme> schemes = fundSchemeRepository.findByManager_Id(id);
        List<FundSchemeResponseDto>resultScheme =schemes.stream()
                .map(this::toFundSchemeDto)
                .collect(Collectors.toList());

        List<Company>companyData=companyRepository.findAll();
        for (FundSchemeResponseDto scheme : resultScheme) {
            BigDecimal totalCapital = scheme.getAum() != null ? scheme.getAum() : BigDecimal.ZERO;
            BigDecimal totalPnL = BigDecimal.ZERO;

            if (scheme.getCompaniesInvestedIn() != null) {
                for (CompanyInvestmentDto investment : scheme.getCompaniesInvestedIn()) {
                    Long companyId = investment.getCompanyId();
                    Company company = companyData.stream()
                            .filter(c -> c.getId().equals(companyId))
                            .findFirst()
                            .orElse(null);

                    if (company != null && company.getNav() != null && investment.getNumberOfStocks() != null) {
                        BigDecimal nav = company.getNav();
                        BigDecimal stockQty = BigDecimal.valueOf(investment.getNumberOfStocks());
                        BigDecimal currentValue = nav.multiply(stockQty);

                        totalCapital = totalCapital.add(currentValue);

                        BigDecimal investedAmount = investment.getInvestedAmount() != null
                                ? investment.getInvestedAmount()
                                : BigDecimal.ZERO;

                        BigDecimal pnl = currentValue.subtract(investedAmount);
                        totalPnL = totalPnL.add(pnl);
                    }
                }
            }

            scheme.setTotalCapital(totalCapital);
            scheme.setPnl(totalPnL);
        }



        return resultScheme;
    }

    private FundSchemeResponseDto toFundSchemeDto(FundScheme fs) {
        return FundSchemeResponseDto.builder()
                .id(fs.getId())
                .name(fs.getName())
                .type(fs.getType())
                .objective(fs.getObjective())
                .aum(fs.getAum())
                .currentNav(fs.getCurrentNav())
                .riskLevel(fs.getRiskLevel())
                .expenseRatio(fs.getExpenseRatio())
                .exitLoad(fs.getExitLoad())
                .lockInPeriod(fs.getLockInPeriod())
                .minInvestment(fs.getMinInvestment())
                .minSipAmount(fs.getMinSipAmount())
                .benchmarkIndex(fs.getBenchmarkIndex())
                .launchDate(fs.getLaunchDate())
                .category(fs.getCategory())
                .status(fs.getStatus())
                .amcId(fs.getAmc().getId())
                .managerId(fs.getManager() != null ? fs.getManager().getId() : null)
                .createdAt(fs.getCreatedAt())
                .updatedAt(fs.getUpdatedAt())
                .companiesInvestedIn(fs.getCompaniesInvestedIn().stream()
                        .map(ci -> CompanyInvestmentDto.builder()
                                .id(ci.getId())
                                .companyId(ci.getCompanyId())
                                .companyName(ci.getCompanyName())
                                .investedAmount(ci.getInvestedAmount())
                                .numberOfStocks(ci.getNumberOfStocks())
                                .investmentDate(ci.getInvestmentDate())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public FundSchemeResponseDto UpdateSchemeById(String id, FundSchemeResponseDto dto) {
        FundScheme fs = fundSchemeRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found with ID: " + id));

        fs.setName(dto.getName());
        fs.setType(dto.getType());
        fs.setObjective(dto.getObjective());
        fs.setAum(dto.getAum());
        fs.setCurrentNav(dto.getCurrentNav());
        fs.setRiskLevel(dto.getRiskLevel());
        fs.setExpenseRatio(dto.getExpenseRatio());
        fs.setExitLoad(dto.getExitLoad());
        fs.setLockInPeriod(dto.getLockInPeriod());
        fs.setMinInvestment(dto.getMinInvestment());
        fs.setMinSipAmount(dto.getMinSipAmount());
        fs.setBenchmarkIndex(dto.getBenchmarkIndex());
        fs.setLaunchDate(dto.getLaunchDate());
        fs.setCategory(dto.getCategory());
        fs.setStatus(dto.getStatus());

        if (dto.getCompaniesInvestedIn() != null) {
            fs.getCompaniesInvestedIn().clear();

            for (var investmentDto : dto.getCompaniesInvestedIn()) {
                CompanyInvestment investment;

                if (investmentDto.getId() != null &&
                        companyInvestmentRepository.existsById(investmentDto.getId())) {
                    investment = companyInvestmentRepository.findById(investmentDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("CompanyInvestment not found"));
                    investment.setCompanyName(investmentDto.getCompanyName());
                    investment.setInvestedAmount(investmentDto.getInvestedAmount());
                    investment.setNumberOfStocks(investmentDto.getNumberOfStocks());
                    investment.setInvestmentDate(investmentDto.getInvestmentDate());
                } else {
                    investment = CompanyInvestment.builder()
                            .companyId(investmentDto.getCompanyId())
                            .companyName(investmentDto.getCompanyName())
                            .investedAmount(investmentDto.getInvestedAmount())
                            .numberOfStocks(investmentDto.getNumberOfStocks())
                            .investmentDate(investmentDto.getInvestmentDate())
                            .fundScheme(fs)
                            .build();
                }

                investment.setFundScheme(fs);
                investment = companyInvestmentRepository.save(investment);
                fs.getCompaniesInvestedIn().add(investment);
            }
        }

        if (dto.getAmcId() != null) {
            AMC amc = amcRepository.findById(dto.getAmcId())
                    .orElseThrow(() -> new ResourceNotFoundException("AMC not found with ID: " + dto.getAmcId()));
            fs.setAmc(amc);
        }

        if (dto.getManagerId() != null) {
            FundManager manager = fundManagerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found with ID: " + dto.getManagerId()));
            fs.setManager(manager);
        }

        FundScheme updatedScheme = fundSchemeRepository.save(fs);
        return toFundSchemeDto(updatedScheme);
    }


    @Override
    public CompanyInvestmentDto buyStocks(String id, CompanyInvestmentDto dto) {
        Company c = companyRepository.findCompanyById(dto.getCompanyId());
        FundScheme fs = fundSchemeRepository.findById(dto.getFundSchemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found with ID: " + dto.getFundSchemeId()));

        Integer nS = dto.getNumberOfStocks();
        BigDecimal investmentAmount = c.getNav().multiply(BigDecimal.valueOf(nS));
        BigDecimal aUm = fs.getAum();

        CompanyInvestment existingInvestment = companyInvestmentRepository.findByCompanyIdAndFundScheme_Id(c.getId(), fs.getId());
        CompanyInvestmentDto resultDto;

        FundManagerTransaction.FundManagerTransactionBuilder transactionBuilder = FundManagerTransaction.builder()
                .fundManager(fs.getManager())
                .fundScheme(fs)
                .fundSchemeName(fs.getName())
                .companyId(c.getId().toString())
                .companyName(c.getName())
                .transactionType("BUY")
                .numberOfStocks(nS)
                .pricePerStock(c.getNav())
                .totalValue(investmentAmount)
                .transactionDate(LocalDateTime.now());

        if (investmentAmount.compareTo(aUm) <= 0) {
            fs.setAum(aUm.subtract(investmentAmount));
            if (existingInvestment != null) {

                existingInvestment.setNumberOfStocks(existingInvestment.getNumberOfStocks() + nS);
                existingInvestment.setInvestedAmount(existingInvestment.getInvestedAmount().add(investmentAmount));
                companyInvestmentRepository.save(existingInvestment);
                resultDto = CompanyInvestmentDto.builder()
                        .id(existingInvestment.getId())
                        .companyId(existingInvestment.getCompanyId())
                        .companyName(existingInvestment.getCompanyName())
                        .investedAmount(existingInvestment.getInvestedAmount())
                        .numberOfStocks(existingInvestment.getNumberOfStocks())
                        .investmentDate(existingInvestment.getInvestmentDate())
                        .fundSchemeId(fs.getId())
                        .build();
            } else {
                CompanyInvestment newInvestment = CompanyInvestment.builder()
                        .companyId(c.getId())
                        .companyName(c.getName())
                        .investedAmount(investmentAmount)
                        .numberOfStocks(nS)
                        .investmentDate(LocalDate.now())
                        .fundScheme(fs)
                        .build();
                CompanyInvestment saved = companyInvestmentRepository.save(newInvestment);
                System.out.println("newInvestment"+saved);
                resultDto = CompanyInvestmentDto.builder()
                        .id(saved.getId())
                        .companyId(saved.getCompanyId())
                        .companyName(saved.getCompanyName())
                        .investedAmount(saved.getInvestedAmount())
                        .numberOfStocks(saved.getNumberOfStocks())
                        .investmentDate(saved.getInvestmentDate())
                        .fundSchemeId(fs.getId())
                        .build();
            }

            transactionBuilder.status("SUCCESS");
            fundManagerTransactionRepository.save(transactionBuilder.build());
            fundSchemeRepository.save(fs);
            return resultDto;
        } else {
            transactionBuilder.status("FAILED");
            fundManagerTransactionRepository.save(transactionBuilder.build());
            throw new IllegalArgumentException("Insufficient AUM in fund scheme to buy stocks.");
        }
    }

    @Override
    public CompanyInvestmentDto sellStocks(String id, Long companyId, Integer stocksToSell) {
        FundScheme fs = fundSchemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found with ID: " + id));

        CompanyInvestment investment = companyInvestmentRepository.findByCompanyId(companyId);
        Company company = companyRepository.findCompanyById(companyId);

        BigDecimal nav = company.getNav();
        BigDecimal sellAmount = nav.multiply(BigDecimal.valueOf(stocksToSell));

        FundManagerTransaction.FundManagerTransactionBuilder transactionBuilder = FundManagerTransaction.builder()
                .fundManager(fs.getManager())
                .fundScheme(fs)
                .fundSchemeName(fs.getName())
                .companyId(company.getId().toString())
                .companyName(company.getName())
                .transactionType("SELL")
                .numberOfStocks(stocksToSell)
                .pricePerStock(nav)
                .totalValue(sellAmount)
                .transactionDate(LocalDateTime.now());

        if (investment == null || investment.getFundScheme() == null ||
                !investment.getFundScheme().getId().equals(fs.getId())) {
            transactionBuilder.status("FAILED");
            fundManagerTransactionRepository.save(transactionBuilder.build());
            throw new IllegalArgumentException("No such investment under the specified fund scheme.");
        }

        Integer ownedStocks = investment.getNumberOfStocks();
        if (stocksToSell > ownedStocks) {
            transactionBuilder.status("FAILED");
            fundManagerTransactionRepository.save(transactionBuilder.build());
            throw new IllegalArgumentException("Not enough stocks to sell.");
        }

        fs.setAum(fs.getAum().add(sellAmount));
        fundSchemeRepository.save(fs);

        if (stocksToSell.equals(ownedStocks)) {
            companyInvestmentRepository.delete(investment);
        } else {
            investment.setNumberOfStocks(ownedStocks - stocksToSell);
            BigDecimal avgPricePerStock = investment.getInvestedAmount().divide(BigDecimal.valueOf(ownedStocks), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal amountToSubtract = avgPricePerStock.multiply(BigDecimal.valueOf(stocksToSell));
            investment.setInvestedAmount(investment.getInvestedAmount().subtract(amountToSubtract));
            companyInvestmentRepository.save(investment);
        }

        transactionBuilder.status("SUCCESS");
        fundManagerTransactionRepository.save(transactionBuilder.build());

        return investment == null ? null : CompanyInvestmentDto.builder()
                .id(investment.getId())
                .companyId(investment.getCompanyId())
                .companyName(investment.getCompanyName())
                .investedAmount(investment.getInvestedAmount())
                .numberOfStocks(investment.getNumberOfStocks())
                .investmentDate(investment.getInvestmentDate())
                .fundSchemeId(fs.getId())
                .build();
    }

    @Override
    public StrategyAndBacktestCountDto getStrategyCount(){
        StrategyAndBacktestCountDto result=new StrategyAndBacktestCountDto();
        result.setStrategyCount(strategyRepository.count());
        result.setBacktestCount(backtestResultRepository.count());
        return result;
    }

}
