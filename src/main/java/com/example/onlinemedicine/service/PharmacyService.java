package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.dto.pharmacy.request.PharmacyRequestDto;
import com.example.onlinemedicine.dto.pharmacy.response.PharmacyResponseDto;
import com.example.onlinemedicine.entity.Location;
import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.entity.UserEntity;
import com.example.onlinemedicine.exception.*;
import com.example.onlinemedicine.repository.PharmacyRepository;
import com.example.onlinemedicine.repository.UserRepository;
import com.example.onlinemedicine.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PharmacyService {


    private final PharmacyRepository pharmacyRepository;
    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public PharmacyResponseDto create(PharmacyRequestDto requestDto) {
        PharmacyEntity pharmacyEntity = requestToEntity(requestDto);
        boolean checkingForPharmacyLocation = validationService.checkingForLnLt(pharmacyEntity);
        boolean checkedPhoneNumber = validationService.checkingPhoneNumber(pharmacyEntity.getContact().getPhoneNumber());
        boolean checkedEmail = validationService.checkingEmail(pharmacyEntity.getContact().getEmail());
        if (checkingForPharmacyLocation) {
            throw new DataAlreadyExistsException("This pharmacy is exist");
        }
        if (!checkedPhoneNumber) {
            throw new WrongInputException("You entered phone number incorrect");
        }
        if (!checkedEmail) {
            throw new WrongInputException("You entered invalid email");
        }

        pharmacyRepository.save(pharmacyEntity);
        return entityToResponse(pharmacyEntity);
    }

    public void delete(UUID id) {
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Pharmacy do not found"));

        if (pharmacyEntity.isActive()) {
            pharmacyEntity.setActive(false);
            pharmacyRepository.save(pharmacyEntity);
        }
        throw new WrongInputException("This pharmacy is not exist");

    }

    public PharmacyResponseDto getByIdAndNotDeleted(UUID id) {
        PharmacyEntity pharmacyEntity = checkIsNotDeleted(id);
        if (Objects.isNull(pharmacyEntity)) {
            throw new DataNotFoundException("This pharmacy is not exist");
        }

        return entityToResponse(pharmacyEntity);
    }

    public PharmacyResponseDto getById(UUID id) {
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Pharmacy do not found"));
        return entityToResponse(pharmacyEntity);
    }

    public List<PharmacyResponseDto> getAll(int page, int pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        List<PharmacyResponseDto> responseList = pharmacyRepository.findAll(pageRequest)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());

        return responseList;
    }


    public PharmacyResponseDto updateAndNotDeleted(UUID id, PharmacyRequestDto requestDto) {
        PharmacyEntity pharmacyEntity = checkIsNotDeleted(id);

        if (Objects.isNull(pharmacyEntity)) {
            throw new DataNotFoundException("This pharmacy is not exist");
        }
        modelMapper.map(requestDto, pharmacyEntity);

        ModelMapper modelMapper = new ModelMapper();
        PharmacyEntity pharmacy = modelMapper.map(requestDto, PharmacyEntity.class);
        return entityToResponse(pharmacyRepository.save(pharmacy));
    }


    private PharmacyEntity checkIsNotDeleted(UUID id) {
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Pharmacy do not found"));

        if (pharmacyEntity.isActive()) {
            return pharmacyEntity;
        }
        throw new DataNotFoundException("pharmacy not found while updating it.");

    }


    private PharmacyEntity requestToEntity(PharmacyRequestDto pharmacyRequestDto) {
        PharmacyEntity pharmacyEntity = modelMapper.map(pharmacyRequestDto, PharmacyEntity.class);
        UserEntity owner = userRepository.findById(pharmacyRequestDto.getOwnerId())
                .orElseThrow(() -> new DataNotFoundException("user not found while setting owner to pharmacy."));
        pharmacyEntity.setOwner(owner);
        return pharmacyEntity;

    }

    private PharmacyResponseDto entityToResponse(PharmacyEntity pharmacyEntity) {
        PharmacyResponseDto pharmacyResponseDto = modelMapper.map(pharmacyEntity, PharmacyResponseDto.class);
        Set<UUID> medicineResponseDtos=new HashSet<>();
        for (MedicineEntity medicine : pharmacyEntity.getMedicines()) {
            medicineResponseDtos.add(medicine.getId());
        }
        pharmacyResponseDto.setMedicineId(medicineResponseDtos);
        return pharmacyResponseDto;
    }


    public List<PharmacyResponseDto> findPharmaciesWithinRadius(Location myLocation, double radiusInKm/*,List<PharmacyEntity> pharmacies*/) {
        List<PharmacyEntity> pharmacies = pharmacyRepository.findAll();
        List<PharmacyEntity> pharmaciesWithinRadius = new ArrayList<>();
        List<PharmacyResponseDto> pharmaciesWithinRadiusDto = new ArrayList<>();

        for (PharmacyEntity pharmacy : pharmacies) {
            double distance = calculateHaversineDistance(myLocation, pharmacy.getLocation());

            if (distance <= radiusInKm) {
                pharmaciesWithinRadius.add(pharmacy);
            }
        }
        for (PharmacyEntity withinRadius : pharmaciesWithinRadius) {
            PharmacyResponseDto pharmacyResponseDto = entityToResponse(withinRadius);
            pharmaciesWithinRadiusDto.add(pharmacyResponseDto);
        }
        return pharmaciesWithinRadiusDto;
    }

    private static double calculateHaversineDistance(Location location1, Location location2) {
        // Implementation of Haversine formula to calculate distance between two points on Earth
        // You can find various implementations online or use a library for more accuracy
        // This is a simple example
        double R = 6371; // Radius of Earth in kilometers

        double lat1 = Math.toRadians(Double.parseDouble(location1.getLatitude()));
        double lon1 = Math.toRadians(Double.parseDouble(location1.getLongitude()));
        double lat2 = Math.toRadians(Double.parseDouble(location2.getLatitude()));
        double lon2 = Math.toRadians(Double.parseDouble(location2.getLongitude()));

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (R * c);
    }

}
