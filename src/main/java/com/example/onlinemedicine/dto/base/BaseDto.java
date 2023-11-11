package com.example.onlinemedicine.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BaseDto {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
}
