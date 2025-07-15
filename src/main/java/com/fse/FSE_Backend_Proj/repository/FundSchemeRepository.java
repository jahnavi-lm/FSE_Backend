package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.FundScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundSchemeRepository extends JpaRepository<FundScheme, String> {
    List<FundScheme> findByAmc_Id(String amcId);
    List<FundScheme> findByManager_Id(String managerId);
}
