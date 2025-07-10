package com.fse.FSE_Backend_Proj.service.impl;

import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
import com.fse.FSE_Backend_Proj.repository.FundSchemeRepository;
import com.fse.FSE_Backend_Proj.repository.InvestorRepository;
import com.fse.FSE_Backend_Proj.service.FundSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundSchemeServiceImpl implements FundSchemeService {

    private final FundSchemeRepository fundSchemeRepo;
    private final AMCRepository amcRepo;
    private final FundManagerRepository fundManagerRepo;
    private final InvestorRepository investorRepo;

    @Override
    public FundSchemeResponseDto create(String amcId, FundSchemeRequestDto dto) {
        AMC amc = amcRepo.findById(amcId)
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found"));

        FundScheme fs = FundScheme.builder()
                .name(dto.getName())
                .type(dto.getType())
                .objective(dto.getObjective())
                .aum(dto.getAum())
                .currentNav(dto.getCurrentNav())
                .riskLevel(dto.getRiskLevel())
                .expenseRatio(dto.getExpenseRatio())
                .exitLoad(dto.getExitLoad())
                .lockInPeriod(dto.getLockInPeriod())
                .minInvestment(dto.getMinInvestment())
                .minSipAmount(dto.getMinSipAmount())
                .benchmarkIndex(dto.getBenchmarkIndex())
                .launchDate(dto.getLaunchDate())
                .category(dto.getCategory())
                .status(dto.getStatus())
                .amc(amc)
                .investors(new ArrayList<>())
                .companiesInvestedIn(new ArrayList<>())
                .build();

        return toDto(fundSchemeRepo.save(fs));
    }

    @Override
    public FundSchemeResponseDto getById(String id) {
        return toDto(getEntity(id));
    }

    @Override
    public List<FundSchemeResponseDto> getAll() {
        return fundSchemeRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FundSchemeResponseDto> getByAmc(String amcId) {
        return fundSchemeRepo.findByAmc_Id(amcId).stream().map(this::toDto).collect(Collectors.toList());
    }

//    @Override
//    public FundSchemeResponseDto update(String id, FundSchemeRequestDto dto) {
//        FundScheme fs = getEntity(id);
//        fs.setName(dto.getName());
//        fs.setType(dto.getType());
//        fs.setObjective(dto.getObjective());
//        fs.setAum(dto.getAum());
//        fs.setCurrentNav(dto.getCurrentNav());
//        fs.setRiskLevel(dto.getRiskLevel());
//        fs.setExpenseRatio(dto.getExpenseRatio());
//        fs.setExitLoad(dto.getExitLoad());
//        fs.setLockInPeriod(dto.getLockInPeriod());
//        fs.setMinInvestment(dto.getMinInvestment());
//        fs.setMinSipAmount(dto.getMinSipAmount());
//        fs.setBenchmarkIndex(dto.getBenchmarkIndex());
//        fs.setLaunchDate(dto.getLaunchDate());
//        fs.setCategory(dto.getCategory());
//        fs.setStatus(dto.getStatus());
//
//        List<CompanyInvestment> investments = dto.getCompaniesInvestedIn().stream()
//                .map(c -> CompanyInvestment.builder()
//                        .companyName(c.getCompanyName())
//                        .investedAmount(c.getInvestedAmount())
//                        .numberOfStocks(c.getNumberOfStocks())
//                        .investmentDate(c.getInvestmentDate())
//                        .fundScheme(fs)
//                        .build())
//                .toList();
//        fs.setCompaniesInvestedIn(investments);
//
//        if (dto.getInvestorIds() != null) {
//            List<Investor> investors = dto.getInvestorIds().stream()
//                    .map(idVal -> investorRepo.findById(idVal)
//                            .orElseThrow(() -> new ResourceNotFoundException("Investor not found with ID: " + idVal)))
//                    .toList();
//
//            investors.forEach(inv -> inv.setFundScheme(fs));
//            fs.setInvestors(investors);
//        } else {
//            fs.setInvestors(new ArrayList<>());
//        }
//
//        return toDto(fundSchemeRepo.save(fs));
//    }


    @Override
    public FundSchemeResponseDto update(String id, FundSchemeRequestDto dto) {
        FundScheme fs = getEntity(id);

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

        List<CompanyInvestment> investments = dto.getCompaniesInvestedIn() != null
                ? dto.getCompaniesInvestedIn().stream()
                .map(c -> CompanyInvestment.builder()
                        .companyName(c.getCompanyName())
                        .investedAmount(c.getInvestedAmount())
                        .numberOfStocks(c.getNumberOfStocks())
                        .investmentDate(c.getInvestmentDate())
                        .fundScheme(fs)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();

        // FIX: Clear the old list and add new
        if (fs.getCompaniesInvestedIn() != null) {
            fs.getCompaniesInvestedIn().clear();
            fs.getCompaniesInvestedIn().addAll(investments);
        } else {
            fs.setCompaniesInvestedIn(investments);
        }

        if (dto.getInvestorIds() != null) {
            List<Investor> investors = dto.getInvestorIds().stream()
                    .map(idVal -> investorRepo.findById(idVal)
                            .orElseThrow(() -> new ResourceNotFoundException("Investor not found with ID: " + idVal)))
                    .collect(Collectors.toList());

            investors.forEach(inv -> inv.setFundScheme(fs));

            if (fs.getInvestors() != null) {
                fs.getInvestors().clear();
                fs.getInvestors().addAll(investors);
            } else {
                fs.setInvestors(investors);
            }
        } else {
            if (fs.getInvestors() != null) {
                fs.getInvestors().clear();  // clear if exists
            } else {
                fs.setInvestors(new ArrayList<>());
            }
        }

        return toDto(fundSchemeRepo.save(fs));
    }


    @Override
    public void delete(String id) {
        fundSchemeRepo.delete(getEntity(id));
    }

    @Override
    public FundSchemeResponseDto assignManager(String schemeId, String managerId) {
        FundScheme fs = getEntity(schemeId);
        FundManager manager = fundManagerRepo.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        fs.setManager(manager);
        return toDto(fundSchemeRepo.save(fs));
    }

    private FundScheme getEntity(String id) {
        return fundSchemeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found"));
    }

    private FundSchemeResponseDto toDto(FundScheme fs) {
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
                .build();
    }

    @Override
    public List<FundSchemeResponseDto> getByManager(String managerId) {
        return fundSchemeRepo.findByManager_Id(managerId).stream().map(this::toDto).collect(Collectors.toList());
    }

}

