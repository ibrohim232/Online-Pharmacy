package com.example.onlinemedicine.validation;

import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;


    public boolean isValidUserName(String userName) {
        Optional<UserEntity> user = userRepository.findByUserName(userName);
        return user.isEmpty();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        Optional<UserEntity> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return false;
        }
        return Pattern.matches("^+998((0-9){2}|[0-9]{2})[0-9]{7}$", phoneNumber);
    }

    public boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#%])[A-Za-z\\d!#%]{8,}$", password);
    }
    public boolean isValidEmail(String email) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!#%])[A-Za-z\\d!#%]{8,}$", email);
    }
}
