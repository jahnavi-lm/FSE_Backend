package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;

import java.util.List;

public interface FundManagerService {
    FundManagerResponseDto create(FundManagerRequestDto dto);
    FundManagerResponseDto getById(String id);
    List<FundManagerResponseDto> getAll();
    FundManagerResponseDto update(String id, FundManagerRequestDto dto);
    void delete(String id);
    List<FundSchemeResponseDto> getSchemesByFundManagerId(String id);
}
