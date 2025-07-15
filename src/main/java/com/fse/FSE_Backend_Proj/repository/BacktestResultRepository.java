package com.fse.FSE_Backend_Proj.repository;


import com.fse.FSE_Backend_Proj.model.BacktestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BacktestResultRepository extends JpaRepository<BacktestResult, Long> {
    // Optional — use only if you want to persist past results

    Optional<BacktestResult> findByStrategyId(Long strategyId);

}
