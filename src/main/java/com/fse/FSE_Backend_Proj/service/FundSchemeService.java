package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;

import java.util.List;

public interface FundSchemeService {
    FundSchemeResponseDto create(String amcId, FundSchemeRequestDto dto);
    FundSchemeResponseDto getById(String id);
    List<FundSchemeResponseDto> getAll();
    List<FundSchemeResponseDto> getByAmc(String amcId);
    FundSchemeResponseDto update(String id, FundSchemeRequestDto dto);
    void delete(String id);
    FundSchemeResponseDto assignManager(String schemeId, String managerId);
    List<FundSchemeResponseDto> getByManager(String managerId);
}
