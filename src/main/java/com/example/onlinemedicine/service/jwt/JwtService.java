package com.example.onlinemedicine.service.jwt;


import com.example.onlinemedicine.dto.user.JwtRequestDto;
import com.example.onlinemedicine.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.expiry}")
    private Integer expiry;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(JwtRequestDto jwtRequestDto) {
        Date date = new Date();
        return Jwts.builder()
                .setSubject(jwtRequestDto.getId().toString())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + expiry))
                .addClaims(getAuthorities(jwtRequestDto))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> getAuthorities(JwtRequestDto jwtRequestDto) {
        return Map.of("roles",
                jwtRequestDto.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
    }

    public Jws<Claims> extractToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
    }
}
