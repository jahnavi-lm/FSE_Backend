package com.fse.FSE_Backend_Proj.util;

import com.fse.FSE_Backend_Proj.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth") || path.equals("/api/investors");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isTokenValid(token)) {
                    String subject = jwtUtil.extractUsername(token); // e.g., "investor@gmail.com:INVESTOR"
                    String[] parts = subject.split(":");

                    if (parts.length == 2) {
                        String email = parts[0];
                        String role = parts[1];

                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + role)) // Spring expects ROLE_ prefix
                                );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        System.out.println("Invalid token format: expected email:ROLE");
                    }
                }
            } catch (Exception e) {
                System.out.println("JWT Authentication error: " + e.getMessage());
                // Optional: respond with 401
                // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                // return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
