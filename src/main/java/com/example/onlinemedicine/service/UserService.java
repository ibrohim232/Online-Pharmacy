package com.example.onlinemedicine.service;


import com.example.onlinemedicine.dto.base.JwtResponse;
import com.example.onlinemedicine.dto.base.MailDto;
import com.example.onlinemedicine.dto.user.*;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.exception.WrongInputException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.jwt.AuthenticationService;
import com.example.onlinemedicine.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Random random = new Random();
    @Value("${services.notification-url}")
    private String notificationServiceUrl;
    private final AuthenticationService authenticationService;

//    @Transactional
//    public UserResponseDto singUp(UserRequestDto createReq) {
//        UserEntity user = mapCRToEntity(createReq);
//        user.setCode(random.nextInt(1000, 10000));
//
//        if (createReq.getEmail() == null) {
//            throw new WrongInputException("email is null");
//        }
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<MailDto> requestEntity = new HttpEntity<>(new MailDto(String.valueOf(user.getCode()), user.getEmail()), headers);
//        ResponseEntity<Void> response = restTemplate.postForEntity(
//                notificationServiceUrl, requestEntity, Void.class
//        );
//        if (response.getStatusCode().equals(HttpStatus.OK)) {
//            UserEntity save = repository.save(user);
//            return mapEntityToRES(save);
//        }
//        throw new RuntimeException();
//    }

    public JwtResponse singIn(SingIdDto singIdDto) {
        try {
            UserEntity userEntity = repository.findByUserName(singIdDto.getUserName()).orElseThrow(() -> new DataNotFoundException("user not found"));
            if (!userEntity.isVerify()) {
                throw new WrongInputException("isValidate is false");
            }
            if (!passwordEncoder.matches(singIdDto.getPassword(), userEntity.getPassword()))
                throw new RuntimeException();
            return new JwtResponse(jwtService.generateToken(userEntity));
        } catch (Exception e) {
            throw new WrongInputException("Username or password incorrect");
        }
    }

    public JwtResponse verify(String email, int code) {
        UserEntity user = repository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("user not found"));
        if (user.getCode() == code) {
            user.setVerify(true);
            repository.save(user);
            return new JwtResponse(jwtService.generateToken(user));
        }
        throw new WrongInputException("incorrect code");
    }

    public List<UserResponseDto> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<UserEntity> page = repository.findAll(pageable);
        return page.get().map(this::mapEntityToRES).toList();
    }

    public UserRoleAndPermissionsDto extractToken(String token) {
        Claims claims = jwtService.extractToken(token).getBody();
        String userId = claims.getSubject();
        List<String> roles = (List<String>) claims.get("roles");
        return new UserRoleAndPermissionsDto(userId, roles);
    }

    public UserResponseDto updateUserRole(UpdateUserRoleDto dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setRole(dto.getRole());
        repository.save(user);
        return mapEntityToRES(user);
    }

    public UserResponseDto updateUserPermissions(UpdateUserPermissionsDto dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setPermissions(dto.getPermissions());
        repository.save(user);
        return mapEntityToRES(user);
    }

    protected UserResponseDto mapEntityToRES(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getCreatedData(),
                entity.getUpdatedDate(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getPhoneNumber());
    }


    protected UserEntity mapCRToEntity(UserRequestDto createReq) {
        UserEntity map = modelMapper.map(createReq, UserEntity.class);
        map.setPassword(passwordEncoder.encode(map.getPassword()));
        return map;
    }


}
