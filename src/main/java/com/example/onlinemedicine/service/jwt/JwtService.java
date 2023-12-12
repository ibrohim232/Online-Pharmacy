package com.example.onlinemedicine.service.jwt;


import com.example.onlinemedicine.dto.user.JwtResponseDto;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {
    @Value("${jwt.expiry}")
    private Integer expiry;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(UserEntity user) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + expiry))
                .addClaims(getAuthorities(user))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshToken(UserEntity user){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return Jwts
                .builder()
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .setSubject(user.getId().toString())
                .setIssuedAt(date)
                .setExpiration(calendar.getTime())
                .compact();
    }

    public Map<String, Object> getAuthorities(UserEntity user) {
        return Map.of("roles",
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());
    }



    public Jws<Claims> extractToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
    }

    public boolean isTokenExpired(String token) {
        Jws<Claims> claimsJws = extractToken(token);
        Claims body = claimsJws.getBody();
        Date expiration = body.getExpiration();
        return expiration.after(new Date(System.currentTimeMillis()));
    }

}
