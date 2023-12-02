package com.example.onlinemedicine.validation;

import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.enums.AdviceType;
import com.example.onlinemedicine.entity.enums.MeasurementType;
import com.example.onlinemedicine.entity.enums.MedicineType;
import com.example.onlinemedicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MedicineValidator {
    private final MedicineRepository repository;

    public boolean isUniqueMedicine(String name,
                                    AdviceType adviceType,
                                    MeasurementType measurementType,
                                    MedicineType medicineType,
                                    UUID pharmacyId,
                                    String manufacturer,
                                    String manufactured) {
        Optional<MedicineEntity> medicine = repository.findByNameAndAdviceTypeAndMeasurementTypeAndMedicineTypeAndPharmacyIdAndManufacturerAndManufactured(
                name, adviceType, measurementType, medicineType, pharmacyId, manufacturer, manufactured);
        return medicine.isEmpty();
    }

    public boolean isValidDate(LocalDate bestBefore, LocalDate issuedAt) {
        return !bestBefore.isBefore(issuedAt) && !bestBefore.equals(issuedAt);
    }

}
