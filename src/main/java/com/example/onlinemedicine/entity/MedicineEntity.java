package com.example.onlinemedicine.entity;


import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "medicines", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "adviceType", "measurementType", "medicineType","pharmacy_id"})
})
public class MedicineEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private PharmacyEntity pharmacy;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String manufactured;
    @Column(nullable = false)
    private String manufacturer;
    @Enumerated(EnumType.STRING)
    private AdviceType adviceType;
    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;
    @Enumerated(EnumType.STRING)
    private MedicineType medicineType;
    @Column(nullable = false)
    private LocalDateTime bestBefore;
    @Column(nullable = false)
    private LocalDateTime issuedAt;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private int count;

}
