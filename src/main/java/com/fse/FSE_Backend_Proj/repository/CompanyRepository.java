package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsBySymbol(String symbol);
    Optional findBySymbol(String symbol);
    Company findCompanyById(Long id);// ✅ Enables fetching Company entity by symbol
}
