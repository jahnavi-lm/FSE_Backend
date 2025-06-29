package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.AMC;
import com.fse.FSE_Backend_Proj.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, String> {

}

