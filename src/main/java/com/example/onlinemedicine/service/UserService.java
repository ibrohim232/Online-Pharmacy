package com.example.onlinemedicine.service;


import com.example.onlinemedicine.dto.user.JwtResponseDto;
import com.example.onlinemedicine.dto.user.*;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.exception.WrongInputException;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.service.jwt.AuthenticationService;
import com.example.onlinemedicine.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;

    @Transactional
    public UserResponseDto singUp(UserRequestDto createReq) {
        UserEntity user = mapCRToEntity(createReq);
        user.setCode(random.nextInt(1000, 10000));
        repository.save(user);
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
        user.setRoles(dto.getRole());
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
        notificationService.sendVerifyCode(userEntity.getEmail(), userEntity.getCode());
    }

    public JwtResponseDto generateToken(JwtRequestDto jwtRequestDto) {
        return new JwtResponseDto(jwtService.generateToken(jwtRequestDto));
    }

    protected UserResponseDto mapEntityToRES(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getCreatedData(),
                entity.getUpdatedDate(),
                entity.getFullName(),
                entity.getUsername(),
                entity.getPhoneNumber(),
                entity.getRoles());
    }


    protected UserEntity mapCRToEntity(UserRequestDto createReq) {
        UserEntity map = modelMapper.map(createReq, UserEntity.class);
        map.setPassword(passwordEncoder.encode(map.getPassword()));
        return map;
    }


}
