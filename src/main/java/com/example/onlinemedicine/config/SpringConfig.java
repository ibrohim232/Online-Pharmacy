package com.example.onlinemedicine.config;

import com.example.onlinemedicine.service.jwt.AuthenticationService;
import com.example.onlinemedicine.service.jwt.JwtFilter;
import com.example.onlinemedicine.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SpringConfig {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    private final String[] WHITE_LIST = {"/auth/sign-up",
            "/auth/sign-in",
            "/auth/verify",
            "/extract-token",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/user/extract-token",
            "/auth/generate-token",
            "/auth/get-verify-code"};

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requestConfigurer -> {
                    requestConfigurer
                            .requestMatchers(WHITE_LIST).permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(new JwtFilter(jwtService, authenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }

}
