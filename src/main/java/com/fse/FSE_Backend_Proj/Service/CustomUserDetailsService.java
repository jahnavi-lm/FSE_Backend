package com.fse.FSE_Backend_Proj.Service;

//
//
//import com.fundmanager.fund.Repository.UserRepository;
//import com.fundmanager.fund.controller.Role;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import org.springframework.security.core.userdetails.User; // Import Spring Security's User
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String compositeKey) throws UsernameNotFoundException {
//        String[] parts = compositeKey.split(":");
//        String email = parts[0];
//        Role role = Role.valueOf(parts[1].toUpperCase());
//
//        var user = userRepository.findByEmailAndRole(email, role)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return User.builder()
//                .username(compositeKey)
//                .password(user.getPassword())
//                .roles(role.name())
//                .build();
//    }
//}















import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customUserDetailsServiceImpl.loadUserByCompositeKey(username);
    }
}

