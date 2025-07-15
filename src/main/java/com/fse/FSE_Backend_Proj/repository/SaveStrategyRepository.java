package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.SaveStrategy;
import com.fse.FSE_Backend_Proj.model.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveStrategyRepository extends JpaRepository<Strategy, Long> {
}

