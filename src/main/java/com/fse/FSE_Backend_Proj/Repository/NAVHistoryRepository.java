package com.fse.FSE_Backend_Proj.Repository;

import com.fse.FSE_Backend_Proj.model.NAVHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NAVHistoryRepository extends JpaRepository<NAVHistory, String> {
    List<NAVHistory> findBySchemeIdOrderByDateAsc(String schemeId);
    Optional<NAVHistory> findTopBySchemeIdOrderByDateDesc
            (String schemeId);
}
