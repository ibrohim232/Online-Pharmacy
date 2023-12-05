package com.example.onlinemedicine.dto.contact.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactResponseDto {
    private String phoneNumber;
    private String telegramAccount;
    private String email;
}
