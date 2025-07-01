package com.fse.FSE_Backend_Proj.service.impl;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.FundManager;
import com.fse.FSE_Backend_Proj.model.FundScheme;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
import com.fse.FSE_Backend_Proj.repository.FundSchemeRepository;
import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.service.FundManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

        // Fetch associated user
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        System.out.println("Inside the create ctrl block 2");

        // Check if fund manager already exists for this user
        if (fundManagerRepository.existsById(user.getId())) {
            throw new IllegalStateException("Fund Manager already exists for this user");
        }

        // Create fund manager with shared primary key
        FundManager fm = FundManager.builder()
                .user(user) // @MapsId ensures fm.id = user.id
                .employeeCode(dto.getEmployeeCode())
                .qualification(dto.getQualification())
                .experienceYears(dto.getExperienceYears())
                .bio(dto.getBio())
                .build();

        // Save entity
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
                .build();
    }


}
