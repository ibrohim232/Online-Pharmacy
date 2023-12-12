package com.example.onlinemedicine.config;

import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.entity.enums.Permissions;
import com.example.onlinemedicine.entity.enums.UserRole;
import com.example.onlinemedicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class InitialDates implements CommandLineRunner {
    private final UserRepository userRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    Boolean ddlAuto;

    @Override
    public void run(String... args) throws Exception {
        if(ddlAuto) {
            UserEntity userEntity = new UserEntity();
            userEntity.setVerify(true);
            userEntity.setFullName("Admin Pharmacy");
            userEntity.setEmail("pharmayc@gmail.com");
            ArrayList<Permissions> adminPermissions = new ArrayList<>();
            adminPermissions.add(Permissions.CHANGE_ROLE);
            adminPermissions.add(Permissions.CHANGE_PERMISSION);
            adminPermissions.add(Permissions.USER_GET);
            adminPermissions.add(Permissions.USER_DELETE);
            adminPermissions.add(Permissions.PHARMACY_CREATE);
            adminPermissions.add(Permissions.PHARMACY_DELETE);
            adminPermissions.add(Permissions.PHARMACY_UPDATE);
            userEntity.setPermissions(adminPermissions);
            userEntity.setPhoneNumber("+998997777777");
            userEntity.setRoles(UserRole.SUPER_ADMIN);
            userEntity.setPassword("G26Dorixona");
            userRepository.save(userEntity);
        }
    }
}
