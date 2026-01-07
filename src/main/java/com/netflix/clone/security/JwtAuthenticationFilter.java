package com.netflix.clone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = extractJwtToken(request);
        String username = jwtUtil.getUsernameFromToken(jwt);

        if (shouldProcessAuthentication(username)){
            processAuthentication(request, username, jwt);
        }
        filterChain.doFilter(request, response);
    }

    private void processAuthentication(HttpServletRequest request, String username, String jwt) {
        if (jwtUtil.validateToken(jwt)){
            UserDetails userDetails = createUserDetailsFromToken(jwt, username);
            setAuthenticationInContext(request, userDetails);
        }
    }

    private void setAuthenticationInContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }

    private UserDetails createUserDetailsFromToken(String jwt, String username) {
        String role = jwtUtil.getRoleFromToken(jwt);

        return User.builder()
                .username(username)
                .password("")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("Role_" + role)))
                .build();
    }

    private boolean shouldProcessAuthentication(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private String extractJwtToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        } else if ((requestURI.contains("/api/files/videos") || requestURI.contains("/api/files/images"))
        && request.getParameter("token") != null) {
            return request.getParameter("token");
        } else {
            return null;

        }
    }
}
