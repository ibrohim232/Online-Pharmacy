package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, UUID> {
    Optional<MedicineEntity> findByNameAndAdviceTypeAndMeasurementTypeAndMedicineType(String name, AdviceType adviceType, MeasurementType measurementType, MedicineType medicineType);
}
