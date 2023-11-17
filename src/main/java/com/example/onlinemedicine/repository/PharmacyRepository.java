package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<PharmacyEntity, UUID> {

    Optional<PharmacyEntity> getPharmacyEntityByLocation_LatitudeAndLocation_Longitude(String latitude, String longitude);
}
