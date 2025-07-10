package com.fse.FSE_Backend_Proj.repository;


import com.fse.FSE_Backend_Proj.model.BacktestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BacktestResultRepository extends JpaRepository<BacktestResult, Long> {
    // Optional — use only if you want to persist past results
}
