package com.example.onlinemedicine.dto.location;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequestDto {
    @Column(nullable = false)
    private String latitude;
    @Column(nullable = false)
    private String longitude;
}
