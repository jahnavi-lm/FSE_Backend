package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.FundManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundManagerRepository extends JpaRepository<FundManager, String> {
    boolean existsByEmployeeCode(String employeeCode);
}
