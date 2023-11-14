package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.repository.MedicineRepository;
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
    private final MedicineRepository repository;

    public MedicineResponseDto create(MedicineRequestDto medicineRequestDto) {
        Optional<MedicineEntity> entity = this.repository.findByNameAndAdviceTypeAndMeasurementTypeAndMedicineType(medicineRequestDto.getName(), medicineRequestDto.getAdviceType(), medicineRequestDto.getMeasurementType(), medicineRequestDto.getMedicineType());
        if (entity.isPresent()) {
            throw new RuntimeException();
        } else if (medicineRequestDto.getBestBefore().isBefore(medicineRequestDto.getIssuedAt())) {
            throw new RuntimeException();
        } else {
            MedicineEntity map = (MedicineEntity) this.modelMapper.map(medicineRequestDto, MedicineEntity.class);
            this.repository.save(map);
            return (MedicineResponseDto) this.modelMapper.map(map, MedicineResponseDto.class);
        }
    }

    public MedicineResponseDto findById(UUID id) {
        MedicineEntity medicineEntity = (MedicineEntity) this.repository.findById(id).orElseThrow();
        return (MedicineResponseDto) this.modelMapper.map(medicineEntity, MedicineResponseDto.class);
    }

    public void deleteById(UUID id) {
        this.repository.deleteById(id);
    }

    public List<MedicineResponseDto> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<MedicineEntity> page = this.repository.findAll(pageable);
        return page.get().map((e) -> {
            return new MedicineResponseDto(e.getId(), e.getName(), e.getDescription(), e.getManufactured(), e.getManufacturer(), e.getAdviceType(), e.getMeasurementType(), e.getMedicineType(), e.getBestBefore(), e.getIssuedAt(), e.getPrice(), e.getCount(), e.getCreated(), e.getUpdated());
        }).toList();
    }

}
