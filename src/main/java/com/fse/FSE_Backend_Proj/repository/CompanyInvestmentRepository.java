package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.CompanyInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyInvestmentRepository extends JpaRepository<CompanyInvestment, String> {

    // Optional: fetch all investments by FundScheme ID
    List<CompanyInvestment> findByFundScheme_Id(String fundSchemeId);

    // Optional: fetch all investments by companyId (if needed)
    List<CompanyInvestment> findByCompanyId(String companyId);
}
