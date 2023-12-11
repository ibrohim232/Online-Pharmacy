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
        return userRepository.findByUserName(userName).isEmpty();
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        Optional<UserEntity> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            return false;
        }
        return Pattern.matches("^\\+998((0-9){2}|[0-9]{2})[0-9]{7}$", phoneNumber);
    }

    public boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$", password);

    }

    public boolean isValidEmail(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return false;
        }
        return Pattern.matches("^[a-zA-Z0-9_! #$%&'*+/=?`{|}~^. -]+@[a-zA-Z0-9. -]+$", email);
    }
}
