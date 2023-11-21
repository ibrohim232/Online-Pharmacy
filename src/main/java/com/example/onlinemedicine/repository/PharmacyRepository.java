package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<PharmacyEntity, UUID> {

    Optional<PharmacyEntity> getPharmacyEntityByLocation_LatitudeAndLocation_Longitude(String latitude, String longitude);

    @Query("select ph from  pharmacy ph where (ph.medicineId.name ilike :medicineName) and " +
            "ST_DWithin(" +
            "ST_GeographyFromText('POINT('|| ph.location.longitude || ' ' ||  ph.location.latitude ||')')," +
            "ST_GeographyFromText(:userLocation)," +
            ":maxDistance) = true " +
            "order by ST_Distance(" +
            "ST_GeographyFromText('POINT(' || ph.location.longitude || ' ' || ph.location.latitude || ')')," +
            "ST_GeographyFromText(:userLocation) )" +
            "")
    Optional<List<PharmacyEntity>> getPharmacyEntitiesByNearLocation(
            @Param("userLocation") String userLocation,
            @Param("maxDistance") Double maxDistance,
            @Param("medicineName") String medicineName
    );
}
