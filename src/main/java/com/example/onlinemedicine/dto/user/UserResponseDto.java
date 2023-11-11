package com.example.onlinemedicine.dto.user;


import com.example.onlinemedicine.dto.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto extends BaseDto {
    private String fullName;
    private String userName;
    private String phoneNumber;


    public UserResponseDto(UUID id, LocalDateTime created, LocalDateTime updated, String fullName, String userName, String phoneNumber) {
        super(id, created, updated);
        this.fullName = fullName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;

    }
}
