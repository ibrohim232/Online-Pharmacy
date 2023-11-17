package com.example.onlinemedicine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "contact")
public class Contact extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String telegramAccount;
    @Column(nullable = false, unique = true)
    private String email;
}
