package com.example.onlinemedicine.dto.user;

import com.example.onlinemedicine.entity.enums.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserPermissionsDto {
    private UUID userId;
    private List<Permissions> permissions;
}
