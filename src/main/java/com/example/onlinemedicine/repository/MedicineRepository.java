package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<MedicineEntity, UUID> {
    Optional<MedicineEntity> findByNameAndAdviceTypeAndMeasurementTypeAndMedicineTypeAndPharmacyIdAndManufacturerAndManufacturedAndPrice(String name, AdviceType adviceType, MeasurementType measurementType, MedicineType medicineType, UUID pharmacy_id, String manufacturer, String manufactured, Double price);

    List<MedicineEntity> findByNameStartingWithIgnoreCaseOrderByPriceDesc(String name);

    List<MedicineEntity> findByNameStartingWithIgnoreCaseOrderByPriceAsc(String name);

    List<MedicineEntity> findByNameStartingWithIgnoreCase(String name);


}
