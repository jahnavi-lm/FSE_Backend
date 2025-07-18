package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerRequestDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.FundManagerResponseDto;
import com.fse.FSE_Backend_Proj.dto.fundManagerDto.TotalAmount;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.CompanyInvestmentDto;
import com.fse.FSE_Backend_Proj.dto.fundSchemeDto.FundSchemeResponseDto;
import com.fse.FSE_Backend_Proj.dto.strategyDto.StrategyAndBacktestCountDto;

import java.util.List;

public interface FundManagerService {
    FundManagerResponseDto create(FundManagerRequestDto dto);
    FundManagerResponseDto getById(String id);
    List<FundManagerResponseDto> getAll();
    FundManagerResponseDto update(String id, FundManagerRequestDto dto);
    void delete(String id);
    List<FundSchemeResponseDto> getSchemesByFundManagerId(String id);
    FundSchemeResponseDto UpdateSchemeById (String id,FundSchemeResponseDto dto);
    CompanyInvestmentDto buyStocks(String id,CompanyInvestmentDto dto);
    CompanyInvestmentDto sellStocks(String fundSchemeId, Long companyId, Integer stocksToSell);
    StrategyAndBacktestCountDto getStrategyCount();
}
