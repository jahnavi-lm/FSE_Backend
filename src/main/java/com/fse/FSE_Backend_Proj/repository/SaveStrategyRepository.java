package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.SaveStrategy;
import com.fse.FSE_Backend_Proj.model.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveStrategyRepository extends JpaRepository<Strategy, Long> {
    long count();
}

