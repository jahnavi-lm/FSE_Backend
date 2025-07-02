package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.amcDto.AmcRequestDto;
import com.fse.FSE_Backend_Proj.dto.amcDto.AmcResponseDto;

import java.util.List;

public interface AMCService {
    AmcResponseDto createAmc(String userId, AmcRequestDto dto);
    AmcResponseDto getAmcById(String id);
    List<AmcResponseDto> getAllAmcs();
    AmcResponseDto updateAmc(String id, AmcRequestDto dto);
    void deleteAmc(String id);
}
