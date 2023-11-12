package com.example.onlinemedicine.contoller;


import com.example.onlinemedicine.dto.user.UpdateUserPermissionsDto;
import com.example.onlinemedicine.dto.user.UpdateUserRoleDto;
import com.example.onlinemedicine.dto.user.UserResponseDto;
import com.example.onlinemedicine.dto.user.UserRoleAndPermissionsDto;
import com.example.onlinemedicine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/extract-token")
    public UserRoleAndPermissionsDto extractToken(@RequestParam String token) {
        return userService.extractToken(token);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @PostMapping("/change-role")
    public UserResponseDto changeRole(@RequestBody UpdateUserRoleDto dto) {
        return userService.updateUserRole(dto);
    }

    @PreAuthorize("hasRole('OWNER') and hasAuthority('CHANGE_PERMISSION')")
    @PostMapping("/change-permissions")
    public UserResponseDto changePermissions(@RequestBody UpdateUserPermissionsDto dto) {
        return userService.updateUserPermissions(dto);
    }
}