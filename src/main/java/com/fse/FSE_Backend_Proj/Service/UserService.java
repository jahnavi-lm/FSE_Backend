package com.fse.FSE_Backend_Proj.Service;

import com.fse.FSE_Backend_Proj.Repository.AMCRepository;
import com.fse.FSE_Backend_Proj.Repository.UserRepository;
import com.fse.FSE_Backend_Proj.enums.Role;
import com.fse.FSE_Backend_Proj.entites.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public User registerUser(String username, String fullName, String email, String rawPassword) {
//        User user = User.builder()
//                .username(username)
//                .fullName(fullName)
//                .email(email)                         // 🆕 NEW
//                .passwordHash(passwordEncoder.encode(rawPassword))
//                .role("FUND_MANAGER")
//                .status("ACTIVE")
//                .build();
//
//        return userRepository.save(user);
//    }
//}

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AMCRepository amcRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String name, String email, String password, String role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role.toUpperCase()))
                .kycStatus(false)
                .build();

        return userRepository.save(user);
    }

    public String resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return "Password reset successful";
    }

    public User getUserByEmailAndRole(String email, Role role) {
        return userRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

