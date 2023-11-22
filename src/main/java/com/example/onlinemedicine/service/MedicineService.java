package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.repository.MedicineRepository;
import com.example.onlinemedicine.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MedicineService {
    private final ModelMapper modelMapper;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository repository;

    public MedicineResponseDto create(MedicineRequestDto medicineRequestDto) {
        Optional<MedicineEntity> entity = this.repository.findByNameAndAdviceTypeAndMeasurementTypeAndMedicineTypeAndPharmacyId(
                medicineRequestDto.getName(),
                medicineRequestDto.getAdviceType(),
                medicineRequestDto.getMeasurementType(),
                medicineRequestDto.getMedicineType(),
                medicineRequestDto.getPharmacyId());
        Optional<PharmacyEntity> pharmacyEntity = pharmacyRepository.findById(medicineRequestDto.getPharmacyId());

        if (entity.isPresent() || pharmacyEntity.isEmpty()) {
            throw new RuntimeException();
        } else if (medicineRequestDto.getBestBefore().isBefore(medicineRequestDto.getIssuedAt())) {
            throw new RuntimeException();
        } else {
            MedicineEntity map = requestToEntity(medicineRequestDto);
            map.setPharmacy(pharmacyEntity.get());
            this.repository.save(map);
            return entityToResponse(map);
        }
    }

    public MedicineResponseDto findById(UUID id) {
        MedicineEntity medicineEntity = this.repository.findById(id).orElseThrow();
        return entityToResponse(medicineEntity);
    }

    public void deleteById(UUID id) {
        this.repository.deleteById(id);
    }

    public List<MedicineResponseDto> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<MedicineEntity> page = this.repository.findAll(pageable);
        return page.get().map(this::entityToResponse).toList();
    }

    public List<MedicineResponseDto> findByNameOrderByHigher(String name) {
        List<MedicineEntity> medicineEntities = repository.findByNameOrderByPriceDesc(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    public List<MedicineResponseDto> findByNameOrderByLower(String name) {
        List<MedicineEntity> medicineEntities = repository.findByNameOrderByPriceAsc(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    public List<MedicineResponseDto> findByName(String name) {
        List<MedicineEntity> medicineEntities = repository.findByNameContaining(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    private MedicineResponseDto entityToResponse(MedicineEntity medicineEntity) {
        return modelMapper.map(medicineEntity, MedicineResponseDto.class);
    }

    private MedicineEntity requestToEntity(MedicineRequestDto medicineRequestDto) {
        return modelMapper.map(medicineRequestDto, MedicineEntity.class);
    }


}
