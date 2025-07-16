package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.dto.CompanyDetailsDto;
import com.fse.FSE_Backend_Proj.exception.ResourceNotFoundException;
import com.fse.FSE_Backend_Proj.model.Company;
import com.fse.FSE_Backend_Proj.model.CompanyInvestment;
import com.fse.FSE_Backend_Proj.model.FundScheme;
import com.fse.FSE_Backend_Proj.repository.CompanyInvestmentRepository;
import com.fse.FSE_Backend_Proj.repository.CompanyRepository;
import com.fse.FSE_Backend_Proj.repository.FundSchemeRepository;
import com.fse.FSE_Backend_Proj.service.CompanyExplorerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyExplorerServiceImpl implements CompanyExplorerService {

    private final CompanyRepository companyRepository;
    private final FundSchemeRepository fundSchemeRepository;
    private final CompanyInvestmentRepository companyInvestmentRepository;

    @Override
    public List<CompanyDetailsDto> getCompaniesByIndex(String indexName) {
        List<Company> companies = companyRepository.findByIndexName(indexName.toUpperCase());
        return companies.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CompanyDetailsDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return toDto(company);
    }

    @Override
    public List<CompanyDetailsDto> getCompaniesBySchemeId(String schemeId) {
        FundScheme fs = fundSchemeRepository.findById(schemeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fund Scheme not found"));

        return fs.getCompaniesInvestedIn().stream()
                .map(ci -> CompanyDetailsDto.builder()
                        .id(ci.getCompanyId())
                        .name(ci.getCompanyName())
                        .fundSchemeId(fs.getId())
                        .fundSchemeName(fs.getName())
                        .nav(companyRepository.findCompanyById(ci.getCompanyId()).getNav()) // pulls live NAV
                        .riskFactor(companyRepository.findCompanyById(ci.getCompanyId()).getRiskFactor())
                        .symbol(companyRepository.findCompanyById(ci.getCompanyId()).getSymbol())
                        .indexName(companyRepository.findCompanyById(ci.getCompanyId()).getIndexName())
                        .totalCapital(companyRepository.findCompanyById(ci.getCompanyId()).getTotalCapital())
                        .build())
                .collect(Collectors.toList());
    }

    private CompanyDetailsDto toDto(Company company) {
        return CompanyDetailsDto.builder()
                .id(company.getId())
                .name(company.getName())
                .symbol(company.getSymbol())
                .indexName(company.getIndexName())
                .nav(company.getNav())
                .riskFactor(company.getRiskFactor())
                .totalCapital(company.getTotalCapital())
                .build();
    }
}
