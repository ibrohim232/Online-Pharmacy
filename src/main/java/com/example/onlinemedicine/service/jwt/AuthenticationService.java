package com.example.onlinemedicine.service.jwt;

import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(Claims claims, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userId = claims.getSubject();
        Optional<UserEntity> user = userRepository.findById(UUID.fromString(userId));
        if (user.isEmpty()) {
            response.getWriter().write("User not found");
            return false;
        }
        List<String> roles = (List<String>) claims.get("roles");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userId, null, getAuthorities(roles)
                );
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return true;
    }

    public List<SimpleGrantedAuthority> getAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
