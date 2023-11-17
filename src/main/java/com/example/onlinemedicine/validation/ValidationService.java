package com.example.onlinemedicine.validation;

import com.example.onlinemedicine.entity.Contact;
import com.example.onlinemedicine.entity.Location;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.exception.NotCorrectEmail;
import com.example.onlinemedicine.exception.NotCorrectPhoneNumber;
import com.example.onlinemedicine.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ValidationService {


    private final PharmacyRepository repository;

    public boolean checkingForLnLt(PharmacyEntity pharmacyEntity){
        Location location = pharmacyEntity.getLocation();
        Optional<PharmacyEntity> pharmacy =
                repository.getPharmacyEntityByLocation_LatitudeAndLocation_Longitude(location.getLatitude(), location.getLongitude());
        if (pharmacy.isPresent()){
            return true;
        }

        return false;
    }

    public boolean checkingPhoneNumber(PharmacyEntity pharmacyEntity){
        Contact contact = pharmacyEntity.getContact();
        try {
           return Pattern.matches("^+998((0-9){2}|[0-9]{2})[0-9]{7}$", contact.getPhoneNumber());
        }catch (NotCorrectPhoneNumber e){
            throw new NotCorrectPhoneNumber("Incorrect phone number");
        }
    }

    public boolean checkingEmail(PharmacyEntity pharmacyEntity){
        Contact contact = pharmacyEntity.getContact();
        String email = contact.getEmail();
        try {
            return Pattern.matches("^[a-zA-Z0-9]+([._-][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([.-][a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$", email);
        }catch (NotCorrectEmail e){
            throw new NotCorrectEmail("Incorrect email");
        }
    }
}
