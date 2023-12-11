package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {
    Optional<PhotoEntity> findByMedicineId(UUID uuid);

}
