package com.example.onlinemedicine.dto.midicine;

import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineRequestDto {
    private String name;
    private UUID pharmacyId;
    private String description;
    private String manufactured;
    private String manufacturer;
    private AdviceType adviceType;
    private MeasurementType measurementType;
    private MedicineType medicineType;
    private LocalDateTime bestBefore;
    private LocalDateTime issuedAt;
    private Double price;
    private int count;
}
