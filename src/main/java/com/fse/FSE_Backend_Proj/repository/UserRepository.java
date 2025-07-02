package com.fse.FSE_Backend_Proj.repository;

import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndRole(String email, UserRole userRole);
}
