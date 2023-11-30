package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.mail.EmailDto;
import com.example.onlinemedicine.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/send")
    public void send(@RequestParam String email){
        emailService.sendPassword(email);
    }
    @GetMapping("/verification")
    public String getVerification(@RequestBody EmailDto email){
        return emailService.verified(email);
    }
}
