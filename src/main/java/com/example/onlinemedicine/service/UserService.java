package com.example.onlinemedicine.service;


import com.example.onlinemedicine.dto.user.JwtResponseDto;
import com.example.onlinemedicine.dto.user.*;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.entity.enums.UserRole;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.exception.WrongInputException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.jwt.AuthenticationService;
import com.example.onlinemedicine.service.jwt.JwtService;
import com.example.onlinemedicine.validation.UserValidation;
import io.jsonwebtoken.Claims;
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
            throw new WrongInputException("invalid username");
        }
        if (!validation.isValidPassword(createReq.getPassword())) {
            throw new WrongInputException("invalid password");
        }
        if (!validation.isValidPhoneNumber(createReq.getPhoneNumber())) {
            throw new WrongInputException("invalid phone number");
        }
        if (!validation.isValidEmail(createReq.getEmail())) {
            throw new WrongInputException("invalid email");
        }

        UserEntity user = mapCRToEntity(createReq);
        ArrayList<UserRole> userRoles = new ArrayList<>();
        userRoles.add(UserRole.USER);
        user.setRoles(userRoles);
        user.setCode(random.nextInt(1000, 10000));
        repository.save(user);
        notificationService.sendVerifyCode(user.getEmail(), user.getCode());
        return mapEntityToRES(user);
    }

    public UserResponseDto singIn(SingIdDto singIdDto) {
        try {
            UserEntity userEntity = repository.findByUserName(singIdDto.getUserName()).orElseThrow(() -> new DataNotFoundException("user not found"));
            if (!userEntity.isVerify()) {
                throw new WrongInputException("isValidate is false");
            }
            if (!passwordEncoder.matches(singIdDto.getPassword(), userEntity.getPassword()))
                throw new RuntimeException();
            return mapEntityToRES(userEntity);
        } catch (Exception e) {
            throw new WrongInputException("Username or password incorrect");
        }
    }

    public boolean verify(String email, int code) {
        UserEntity user = repository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("user not found"));
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
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("user not found"));
        List<UserRole> roles = user.getRoles();
        roles.add(dto.getRole());
        user.setRoles(roles);
        repository.save(user);
        return mapEntityToRES(user);
    }

    public UserResponseDto updateUserPermissions(UpdateUserPermissionsDto dto) {
        UserEntity user = repository.findById(dto.getUserId()).orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setPermissions(dto.getPermissions());
        repository.save(user);
        return mapEntityToRES(user);
    }

    public void getVerifyCode(String email) {
        UserEntity userEntity = repository.findByEmail(email).orElseThrow();
        userEntity.setCode(random.nextInt(1000, 10000));
        repository.save(userEntity);
        notificationService.sendVerifyCode(userEntity.getEmail(), userEntity.getCode());
    }

    public JwtResponseDto generateToken(JwtRequestDto jwtRequestDto) {
        return new JwtResponseDto(jwtService.generateToken(jwtRequestDto));
    }

    public UserResponseDto me(UUID id) {
        UserEntity userEntity = repository.findById(id).orElseThrow(() -> new DataNotFoundException("User not found"));
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
