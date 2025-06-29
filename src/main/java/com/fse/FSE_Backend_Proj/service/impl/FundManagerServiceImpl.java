package com.fse.FSE_Backend_Proj.service.impl;


import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.FundManager;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.repository.AMCRepository;
import com.fse.FSE_Backend_Proj.repository.FundManagerRepository;
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

    @Override
    public FundManagerResponseDto create(FundManagerRequestDto dto) {
        User user = userRepository.findById((dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AMC amc = amcRepository.findById(dto.getAmcId())
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found"));

        FundManager fm = FundManager.builder()
                .id(user.getId())
                .user(user)
                .employeeCode(dto.getEmployeeCode())
                .qualification(dto.getQualification())
                .experienceYears(dto.getExperienceYears())
                .bio(dto.getBio())
                .amc(amc)
                .build();

        return toDto(fundManagerRepository.save(fm));
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

        AMC amc = amcRepository.findById(dto.getAmcId())
                .orElseThrow(() -> new ResourceNotFoundException("AMC not found"));

        fm.setEmployeeCode(dto.getEmployeeCode());
        fm.setQualification(dto.getQualification());
        fm.setExperienceYears(dto.getExperienceYears());
        fm.setBio(dto.getBio());
        fm.setAmc(amc);

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
                .amcId(fm.getAmc().getId())
                .build();
    }
}
