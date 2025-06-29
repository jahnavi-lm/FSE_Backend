package com.fse.FSE_Backend_Proj.Repository;

import com.fse.FSE_Backend_Proj.model.UnitLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//public interface UnitLedgerRepository extends JpaRepository<UnitLedger, String> {
//    List<UnitLedger> findByInvestorId(String investorId);
//    Optional<UnitLedger> findByInvestorIdAndSchemeId(String investorId, String schemeId);
//}
public interface UnitLedgerRepository extends JpaRepository<UnitLedger, Long> {
    Optional<UnitLedger> findByInvestorIdAndFundSchemeId(String investorId, String fundSchemeId);

    List<UnitLedger> findByInvestorId(String investorId);
}

