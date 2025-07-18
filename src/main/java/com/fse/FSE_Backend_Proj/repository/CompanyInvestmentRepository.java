package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.CompanyInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyInvestmentRepository extends JpaRepository<CompanyInvestment, String> {
    List<CompanyInvestment> findByFundScheme_Id(String fundSchemeId);
    CompanyInvestment findByCompanyId(Long companyId);
    CompanyInvestment findByCompanyIdAndFundScheme_Id(Long companyId, String fundSchemeId);
}
