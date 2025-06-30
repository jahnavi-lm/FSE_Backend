package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {
    Optional<Investor> findById(String id);
    boolean existsByPanNumber(String panNumber);
}

