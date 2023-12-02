package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.mail.EmailDto;
import com.example.onlinemedicine.exception.IncorrectPassword;
import com.example.onlinemedicine.exception.TimeOut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final RedisTemplate<String, EmailDto> redisTemplate;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void sendPassword(String to){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("YOUR VERIFICATION PASSWORD");
        String password = generatePassword();
        simpleMailMessage.setText(password);
        javaMailSender.send(simpleMailMessage);
        EmailDto emailDto = new EmailDto(to, password);
        redisTemplate.opsForValue().set(emailDto.getEmail(),emailDto,1, TimeUnit.MINUTES);
    }
    public String verified(EmailDto emailEntity){
        EmailDto email = redisTemplate.opsForValue().get(emailEntity.getEmail());
        if(email != null ){
            if(email.getMessage().equals(emailEntity.getMessage())) return "Successfully verification";
            throw new IncorrectPassword("Incorrect email verification");
        }
        throw  new TimeOut("Time out");
    }
    private String generatePassword(){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(0,10));
        }
        return stringBuilder.toString();
    }


}
