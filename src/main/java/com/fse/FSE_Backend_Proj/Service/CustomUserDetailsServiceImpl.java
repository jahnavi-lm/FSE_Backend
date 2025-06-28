package com.fse.FSE_Backend_Proj.Service;

import com.fse.FSE_Backend_Proj.Repository.UserRepository;
import com.fse.FSE_Backend_Proj.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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

        var user = userRepository.findByEmailAndRole(email, userRole)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(compositeKey)
                .password(user.getPasswordHash())
                .roles(userRole.name())
                .build();
    }
}
