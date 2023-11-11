package com.example.onlinemedicine.contoller;

;
import com.example.onlinemedicine.dto.base.JwtResponse;
import com.example.onlinemedicine.dto.user.SingIdDto;
import com.example.onlinemedicine.dto.user.UserRequestDto;
import com.example.onlinemedicine.dto.user.UserResponseDto;
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

//    @PostMapping("/sign-up")
//    public UserResponseDto singUp(@RequestBody UserRequestDto userRequestDto) {
//        return userService.singUp(userRequestDto);
//    }

    @PostMapping("/sign-in")
    public JwtResponse singIn(@RequestBody SingIdDto singIdDto) {
        return userService.singIn(singIdDto);
    }

    @PostMapping("/verify")
    public JwtResponse verify(@RequestParam String email, @RequestParam int code) {
        return userService.verify(email, code);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('USER_GET')")
    @GetMapping("/get-all")
    public List<UserResponseDto> getAll(@RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "1") int page) {
        return userService.getAll(page, size);
    }
}
