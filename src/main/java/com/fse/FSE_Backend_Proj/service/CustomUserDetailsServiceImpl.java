package com.fse.FSE_Backend_Proj.service;

import com.fse.FSE_Backend_Proj.repository.UserRepository;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import com.fse.FSE_Backend_Proj.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl {

    private final UserRepository userRepository;

    public UserDetails loadUserByCompositeKey(String compositeKey) {
        String[] parts = compositeKey.split(":");
        String email = parts[0];
        UserRole userRole = UserRole.valueOf(parts[1].toUpperCase());

        User user = userRepository.findByEmailAndRole(email, userRole)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(compositeKey)
                .password(user.getPasswordHash())
                .roles(userRole.name())
                .build();
    }
}
