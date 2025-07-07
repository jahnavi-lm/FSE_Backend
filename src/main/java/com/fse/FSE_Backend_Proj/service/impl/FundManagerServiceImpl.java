package com.fse.FSE_Backend_Proj.service.impl;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.CompanyInvestmentDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.*;
import com.fse.FSE_Backend_Proj.repository.*;
import com.fse.FSE_Backend_Proj.service.FundManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundManagerServiceImpl implements FundManagerService {

    private final FundManagerRepository fundManagerRepository;
    private final UserRepository userRepository;
    private final AMCRepository amcRepository;
    private final FundSchemeRepository fundSchemeRepository;

    @Override
    public FundManagerResponseDto create(FundManagerRequestDto dto) {
        System.out.println("Inside the create ctrl block 1");

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        System.out.println("Inside the create ctrl block 2");

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

        System.out.println("Inside the create ctrl block 3");
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
        FundManager fm = fundManagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Manager not found"));
        List<FundScheme> schemes = fundSchemeRepository.findByManager_Id(id);
        return schemes.stream()
                .map(this::toFundSchemeDto)
                .collect(Collectors.toList());
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


    private final CompanyInvestmentRepository companyInvestmentRepository;
    @Override
    public FundSchemeResponseDto UpdateSchemeById(String id, FundSchemeResponseDto dto) {
        FundScheme fs = fundSchemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found with ID: " + id));

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
            // Clear existing
            fs.getCompaniesInvestedIn().clear();

            for (var investmentDto : dto.getCompaniesInvestedIn()) {
                CompanyInvestment investment;

                if (investmentDto.getId() != null &&
                        companyInvestmentRepository.existsById(investmentDto.getId())) {
                    // Update existing investment
                    investment = companyInvestmentRepository.findById(investmentDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("CompanyInvestment not found"));
                    investment.setCompanyName(investmentDto.getCompanyName());
                    investment.setInvestedAmount(investmentDto.getInvestedAmount());
                    investment.setNumberOfStocks(investmentDto.getNumberOfStocks());
                    investment.setInvestmentDate(investmentDto.getInvestmentDate());
                } else {
                    // New investment
                    investment = CompanyInvestment.builder()
                            .companyId(investmentDto.getCompanyId())
                            .companyName(investmentDto.getCompanyName())
                            .investedAmount(investmentDto.getInvestedAmount())
                            .numberOfStocks(investmentDto.getNumberOfStocks())
                            .investmentDate(investmentDto.getInvestmentDate())
                            .fundScheme(fs)
                            .build();
                }

                investment.setFundScheme(fs); // ensure fundScheme is always set
                investment = companyInvestmentRepository.save(investment); // save or update
                fs.getCompaniesInvestedIn().add(investment); // link to FundScheme
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



}
