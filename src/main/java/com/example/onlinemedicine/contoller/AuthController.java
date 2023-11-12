package com.example.onlinemedicine.contoller;

;
import com.example.onlinemedicine.dto.user.*;
import com.example.onlinemedicine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public UserResponseDto singUp(@RequestBody UserRequestDto userRequestDto) {
        return userService.singUp(userRequestDto);
    }

    @PostMapping("/sign-in")
    public UserResponseDto singIn(@RequestBody SingIdDto singIdDto) {
        return userService.singIn(singIdDto);
    }

    @PostMapping("/verify")
    public boolean verify(@RequestParam String email, @RequestParam int code) {
        return userService.verify(email, code);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('USER_GET')")
    @GetMapping("/get-all")
    public List<UserResponseDto> getAll(@RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "1") int page) {
        return userService.getAll(page, size);
    }

    @GetMapping("/get-verify-code")
    public void getVerifyCode(@RequestParam String email) {
        userService.getVerifyCode(email);
    }

    @PostMapping("/generate-token")
    public JwtResponseDto generateToken(@RequestBody JwtRequestDto jwtRequestDto) {
        return userService.generateToken(jwtRequestDto);
    }
}
