package com.fse.FSE_Backend_Proj.Repository;

import com.fse.FSE_Backend_Proj.model.FundScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundSchemeRepository extends JpaRepository<FundScheme, String> {
    List<FundScheme> findByTypeAndRiskLevel(String type, String riskLevel);
    List<FundScheme> findByType(String type);
    List<FundScheme> findByRiskLevel(String riskLevel);
}
