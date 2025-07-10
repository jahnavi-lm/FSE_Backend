package com.fse.FSE_Backend_Proj.repository;


import com.fse.FSE_Backend_Proj.model.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrategyRepository extends JpaRepository<Strategy, Long> {
    // Spring Data JPA gives you: save, findById, findAll, delete, etc.
}
