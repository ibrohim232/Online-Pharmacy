package com.example.onlinemedicine.dto.midicine;

import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MedicineResponseDto {
    private UUID id;
    private String name;
    private UUID pharmacyId;
    private String description;
    private String manufactured;
    private String manufacturer;
    private AdviceType adviceType;
    private MeasurementType measurementType;
    private MedicineType medicineType;
    private LocalDate bestBefore;
    private LocalDate issuedAt;
    private Double price;
    private int count;
    private LocalDateTime created;
    private LocalDateTime updated;
    private byte[] imageData;
}
