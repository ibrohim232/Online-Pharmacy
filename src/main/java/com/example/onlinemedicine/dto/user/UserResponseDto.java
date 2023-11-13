package com.example.onlinemedicine.dto.user;


import com.example.onlinemedicine.dto.base.BaseDto;
import com.example.onlinemedicine.entity.enums.Permissions;
import com.example.onlinemedicine.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto extends BaseDto {
    private String fullName;
    private String userName;
    private String phoneNumber;
    private List<UserRole> userRoles;
    private List<Permissions> permissions;


    public UserResponseDto(UUID id, LocalDateTime created, LocalDateTime updated, String fullName, String userName, String phoneNumber, List<UserRole> userRoles, List<Permissions> permissions) {
        super(id, created, updated);
        this.fullName = fullName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userRoles = userRoles;
        this.permissions = permissions;
    }
}
