package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.CompanyDetailsDto;

import java.util.List;

public interface CompanyExplorerService {
    List<CompanyDetailsDto> getCompaniesByIndex(String indexName);
    CompanyDetailsDto getCompanyById(Long id);
    List<CompanyDetailsDto> getCompaniesBySchemeId(String schemeId);
}
