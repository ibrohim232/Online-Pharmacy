package com.example.onlinemedicine.dto.user;

import com.example.onlinemedicine.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserRoleDto {
    private UUID userId;
    private UserRole role;
}
