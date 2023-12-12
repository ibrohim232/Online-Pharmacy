package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.entity.MedicineEntity;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.exception.DataAlreadyExistsException;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.exception.WrongInputException;
import com.example.onlinemedicine.repository.MedicineRepository;
import com.example.onlinemedicine.repository.PharmacyRepository;
import com.example.onlinemedicine.validation.MedicineValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MedicineService {
    private final ModelMapper modelMapper;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository repository;
    private final MedicineValidator validator;
    private final PhotoService photoService;


    public MedicineResponseDto create(MedicineRequestDto medicineRequestDto, MultipartFile file) throws IOException {
        if (!validator.isUniqueMedicine(medicineRequestDto.getName(), medicineRequestDto.getAdviceType(), medicineRequestDto.getMeasurementType(), medicineRequestDto.getMedicineType(), medicineRequestDto.getPharmacyId(), medicineRequestDto.getManufacturer(), medicineRequestDto.getManufactured(), medicineRequestDto.getPrice())) {
            throw new DataAlreadyExistsException("MEDICINE ALREADY EXISTS");
        }
        if (!validator.isValidDate(medicineRequestDto.getBestBefore(), medicineRequestDto.getIssuedAt())) {
            throw new WrongInputException("dori eski");
        }
        Optional<PharmacyEntity> pharmacyEntity = pharmacyRepository.findById(medicineRequestDto.getPharmacyId());
        if (pharmacyEntity.isEmpty()) {
            throw new DataNotFoundException("PHARMACY NOT FOUND");
        }
        MedicineEntity map = requestToEntity(medicineRequestDto);
        map.setPharmacy(pharmacyEntity.get());
        this.repository.save(map);
        String photoPath = photoService.uploadImageToFileSystem(file, map);
        return entityToResponse(map);
    }

    public void setMedicineCount(UUID medicineId, int count) {
        Optional<MedicineEntity> medicine = repository.findById(medicineId);
        if (medicine.isEmpty()) {
            throw new DataNotFoundException("MEDICINE NOT FOUND");
        }
        MedicineEntity medicineEntity = medicine.get();
        medicineEntity.setCount(count);
        repository.save(medicineEntity);
    }

    public MedicineResponseDto findById(UUID id) {
        MedicineEntity medicineEntity = this.repository.findById(id).orElseThrow(() -> new WrongInputException("MEDICINE NOT FOUND"));
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
        List<MedicineEntity> medicineEntities = repository.findByNameStartingWithIgnoreCaseOrderByPriceDesc(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    public List<MedicineResponseDto> findByNameOrderByLower(String name) {
        List<MedicineEntity> medicineEntities = repository.findByNameStartingWithIgnoreCaseOrderByPriceAsc(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    public List<MedicineResponseDto> findByName(String name) {
        List<MedicineEntity> medicineEntities = repository.findByNameStartingWithIgnoreCase(name);
        return medicineEntities.stream().map(this::entityToResponse).toList();
    }

    private MedicineResponseDto entityToResponse(MedicineEntity medicineEntity) {
        MedicineResponseDto map = modelMapper.map(medicineEntity, MedicineResponseDto.class);
        map.setPharmacyId(medicineEntity.getPharmacy().getId());
        try {
            map.setPhotoPath(photoService.downloadImageFromFileSystem(medicineEntity.getId()));
        } catch (IOException ignored) {

        }
        return map;
    }

    private MedicineEntity requestToEntity(MedicineRequestDto medicineRequestDto) {
        return modelMapper.map(medicineRequestDto, MedicineEntity.class);
    }


}
