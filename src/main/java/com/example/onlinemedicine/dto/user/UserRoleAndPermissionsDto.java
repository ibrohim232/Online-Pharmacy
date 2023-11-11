package com.example.onlinemedicine.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserRoleAndPermissionsDto {
    private String userId;
    private List<String> roles;
}
