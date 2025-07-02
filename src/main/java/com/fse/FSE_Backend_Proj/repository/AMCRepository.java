package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.AMC;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

@Repository
public interface AMCRepository extends JpaRepository<AMC, String> {
    boolean existsByRegistrationNo(String registrationNo);
}
