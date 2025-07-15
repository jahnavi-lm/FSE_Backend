package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.FundManagerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundManagerTransactionRepository extends JpaRepository<FundManagerTransaction, String> {

    // Optional: Custom query methods if needed
    List<FundManagerTransaction> findByFundScheme_Id(String id);

    List<FundManagerTransaction> findByFundManager_Id(String id);

    List<FundManagerTransaction> findByCompanyId(String id);
}
