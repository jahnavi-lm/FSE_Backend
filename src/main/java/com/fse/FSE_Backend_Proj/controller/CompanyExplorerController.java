package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.dto.CompanyDetailsDto;
import com.fse.FSE_Backend_Proj.model.Company;
import com.fse.FSE_Backend_Proj.repository.CompanyRepository;
import com.fse.FSE_Backend_Proj.service.CompanyExplorerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyExplorerController {

    private final CompanyExplorerService companyExplorerService;
    private final CompanyRepository companyRepository;

    @GetMapping("/index/{indexName}")
    public ResponseEntity<List<CompanyDetailsDto>> getByIndex(@PathVariable String indexName) {
        return ResponseEntity.ok(companyExplorerService.getCompaniesByIndex(indexName));
    }



    @GetMapping("/{id}")
    public ResponseEntity<CompanyDetailsDto> getByCompanyId(@PathVariable Long id) {
        return ResponseEntity.ok(companyExplorerService.getCompanyById(id));
    }

    @GetMapping("/scheme/{schemeId}")
    public ResponseEntity<List<CompanyDetailsDto>> getByFundScheme(@PathVariable String schemeId) {
        return ResponseEntity.ok(companyExplorerService.getCompaniesBySchemeId(schemeId));
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return ResponseEntity.ok(companies);
    }
}
