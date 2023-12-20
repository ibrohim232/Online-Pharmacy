package com.example.onlinemedicine.service.jwt;

import com.example.onlinemedicine.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(7);
        if (jwtService.isTokenExpired(token)) {
            Jws<Claims> claimsJws = jwtService.extractToken(token);
            boolean authenticate = authenticationService.authenticate(claimsJws.getBody(), request, response);
            if (!authenticate) {
                return;
            }
        } else {
            response.getWriter().write("Token is expired");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
