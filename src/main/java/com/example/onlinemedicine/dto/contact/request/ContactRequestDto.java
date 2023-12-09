package com.example.onlinemedicine.dto.contact.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactRequestDto {
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String telegramAccount;
    @Column(nullable = false, unique = true)
    private String email;
}
