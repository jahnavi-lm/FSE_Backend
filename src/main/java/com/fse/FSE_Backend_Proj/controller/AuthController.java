//package com.fundmanager.fund.controller;
//
//import com.fundmanager.fund.Repository.UserRepository;
//import com.fundmanager.fund.entites.User;
//import com.fundmanager.fund.Service.UserService;
//import com.fundmanager.fund.util.JWTUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final UserService userService;
//    private final AuthenticationManager authManager;
//    private final JWTUtil jwtUtil;
//
//    @PostMapping("/register")
//    public ResponseEntity<User> register(
//            @RequestParam String username,
//            @RequestParam String fullName,
//            @RequestParam String email,
//            @RequestParam String password) {
//        User createdUser = userService.registerUser(username, fullName, email, password);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(
//            @RequestParam String email,
//            @RequestParam String password) {
//        authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password));
//        String token = jwtUtil.generateToken(email);
//        return ResponseEntity.ok(token);
//    }
//}

package com.fse.FSE_Backend_Proj.controller;

import com.fse.FSE_Backend_Proj.Service.UserService;
import com.fse.FSE_Backend_Proj.dto.LoginRequest;
import com.fse.FSE_Backend_Proj.dto.LoginResponse;
import com.fse.FSE_Backend_Proj.dto.RegisterRequest;
import com.fse.FSE_Backend_Proj.dto.ResetPasswordRequest;
import com.fse.FSE_Backend_Proj.entites.User;
import com.fse.FSE_Backend_Proj.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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
                request.getRole().name()
        );
        return ResponseEntity.ok(createdUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
//        String userKey = request.getEmail() + ":" + request.getRole().name();
//
//        authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userKey, request.getPassword())
//        );
//
//        String token = jwtUtil.generateToken(userKey);
//        return ResponseEntity.ok(token);
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String userKey = loginRequest.getEmail() + ":" + loginRequest.getRole().name();

        authManager.authenticate(new UsernamePasswordAuthenticationToken(userKey, loginRequest.getPassword()));
        String token = jwtUtil.generateToken(userKey);
        User user = userService.getUserByEmailAndRole(loginRequest.getEmail(), loginRequest.getRole());

        return ResponseEntity.ok(new LoginResponse(token, user));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ResetPasswordRequest request) {
        String response = userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

}


