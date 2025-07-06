package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.service.UserService;
import com.fse.FSE_Backend_Proj.dto.authDto.LoginRequest;
import com.fse.FSE_Backend_Proj.dto.authDto.LoginResponse;
import com.fse.FSE_Backend_Proj.dto.authDto.RegisterRequest;
import com.fse.FSE_Backend_Proj.dto.authDto.ResetPasswordRequest;
import com.fse.FSE_Backend_Proj.model.User;
import com.fse.FSE_Backend_Proj.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User createdUser = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getUserRole().name()
        );
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Authenticate using "email:ROLE" format, because that's how it's stored in DB (UserDetails)
        String userKey = loginRequest.getEmail() + ":" + user.getRole().name();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(userKey, loginRequest.getPassword()));

        // Generate token using only email and role separately
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(new LoginResponse(token, user));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ResetPasswordRequest request) {
        String response = userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

}

