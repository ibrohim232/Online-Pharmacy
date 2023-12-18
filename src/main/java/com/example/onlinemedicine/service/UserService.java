package com.example.onlinemedicine.service;


import com.example.onlinemedicine.dto.user.JwtResponseDto;
import com.example.onlinemedicine.dto.user.*;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.entity.enums.UserRole;
import com.example.onlinemedicine.exception.DataAlreadyExistsException;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.exception.WrongInputException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.jwt.AuthenticationService;
import com.example.onlinemedicine.service.jwt.JwtService;
import com.example.onlinemedicine.validation.UserValidation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Random random = new Random();
    private final UserValidation validation;
    private final NotificationService notificationService;

    @Transactional
    public UserResponseDto singUp(UserRequestDto createReq) {
        if (!validation.isValidUserName(createReq.getUserName())) {
            throw new DataAlreadyExistsException("Username already exists");
        }
        if (!validation.isValidPassword(createReq.getPassword())) {
            throw new WrongInputException("Password must be at least 8 characters and contain one uppercase,one lowercase , one character");
        }
        if (!validation.isValidPhoneNumber(createReq.getPhoneNumber())) {
            throw new WrongInputException("Phone number should be star +998 and contain 7 numbers ");
        }
        if (!validation.isValidEmail(createReq.getEmail())) {
            throw new DataAlreadyExistsException("Email already exists");
        }
        UserEntity user = mapCRToEntity(createReq);
        user.setRoles(UserRole.USER);
        user.setCode(random.nextInt(1000, 10000));
        repository.save(user);
        notificationService.sendVerifyCode(user.getEmail(), user.getCode());
        return mapEntityToRES(user);
    }

    public JwtResponseDto singIn(SingIdDto singIdDto) {
        UserEntity userEntity = repository.findByUserName(singIdDto.getUserName()).orElseThrow(() -> new DataNotFoundException("USER NOT FOUND"));
        if (!userEntity.isVerify()) {
            throw new WrongInputException("User not verified");
        } else if (!passwordEncoder.matches(singIdDto.getPassword(), userEntity.getPassword()))
            throw new WrongInputException("PASSWORD INCORRECT");
        else {
            return new JwtResponseDto(jwtService.generateToken(userEntity), jwtService.refreshToken(userEntity));
        }
    }

    public String refreshToken(String refreshToken) {
        Jws<Claims> claimsJws = jwtService.extractToken(refreshToken);
        String userId = claimsJws.getBody().getSubject();
        UserEntity user = repository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return jwtService.generateToken(user);
    }


    public boolean verify(String email, int code) {
        UserEntity user = repository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("USER NOT FOUND"));
        if (user.getCode() == code) {
            user.setVerify(true);
            repository.save(user);
            return true;
        }
        throw new WrongInputException("incorrect code");
    }

    public List<UserResponseDto> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<UserEntity> page = repository.findAll(pageable);
        return page.get().map(this::mapEntityToRES).toList();
    }

    public UserResponseDto updateUserRole(UpdateUserRoleDto dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("USER NOT FOUND"));
        user.setRoles(dto.getRole());
        repository.save(user);
        return mapEntityToRES(user);
    }

    public UserResponseDto updateUserPermissions(UpdateUserPermissionsDto dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("USER NOT FOUND"));
        user.setPermissions(dto.getPermissions());
        repository.save(user);
        return mapEntityToRES(user);
    }

    public void getVerifyCode(String email) {
        UserEntity userEntity = repository.findByEmail(email).orElseThrow(() -> new WrongInputException("USER NOT FOUND"));
        userEntity.setCode(random.nextInt(1000, 10000));
        repository.save(userEntity);
        notificationService.sendVerifyCode(userEntity.getEmail(), userEntity.getCode());
    }


    public UserResponseDto me(UUID id) {
        UserEntity userEntity = repository.findById(id).orElseThrow(() -> new DataNotFoundException("USER NOT FOUND"));
        return mapEntityToRES(userEntity);
    }

    protected UserResponseDto mapEntityToRES(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getCreated(),
                entity.getUpdated(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getPhoneNumber(),
                entity.getRoles(),
                entity.getPermissions());
    }


    protected UserEntity mapCRToEntity(UserRequestDto createReq) {
        UserEntity map = modelMapper.map(createReq, UserEntity.class);
        map.setPassword(passwordEncoder.encode(map.getPassword()));
        return map;
    }


}
