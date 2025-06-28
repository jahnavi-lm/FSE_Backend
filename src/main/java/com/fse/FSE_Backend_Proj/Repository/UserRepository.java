package com.fse.FSE_Backend_Proj.Repository;

import com.fse.FSE_Backend_Proj.enums.Role;
import com.fse.FSE_Backend_Proj.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//public interface UserRepository extends JpaRepository<User , Long> {
//    Optional<User> findByUsername(String username);
//    Optional<User> findByEmail(String email);
//}


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRole(String email, Role role);
}
