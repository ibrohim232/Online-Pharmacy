package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.pharmacy.request.PharmacyRequestDto;
import com.example.onlinemedicine.dto.pharmacy.response.PharmacyResponseDto;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.exception.*;
import com.example.onlinemedicine.repository.PharmacyRepository;
import com.example.onlinemedicine.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PharmacyService {


    private final PharmacyRepository pharmacyRepository;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    public PharmacyResponseDto create(PharmacyRequestDto requestDto){
        PharmacyEntity pharmacyEntity = requestToEntity(requestDto);
        boolean checkingForPharmacyLocation = validationService.checkingForLnLt(pharmacyEntity);
        boolean checkedPhoneNumber = validationService.checkingPhoneNumber(pharmacyEntity);
        boolean checkedEmail = validationService.checkingEmail(pharmacyEntity);
        if (checkingForPharmacyLocation){
            throw new AlreadyExistPharmacy("This pharmacy is exist");
        }
        if (!checkedPhoneNumber){
            throw new NotCorrectPhoneNumber("You entered phone number incorrect");
        }
        if (!checkedEmail){
            throw new NotCorrectEmail("You entered invalid email");
        }

        pharmacyRepository.save(pharmacyEntity);
        return entityToResponse(pharmacyEntity);
    }

    public void delete(UUID id){
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFound("Pharmacy do not found"));

        if (pharmacyEntity.isActive()) {
            throw new NotExistPharmacy("This pharmacy is not exist");
        }

        pharmacyEntity.setActive(true);
        pharmacyRepository.save(pharmacyEntity);
    }

    public PharmacyResponseDto getByIdAndNotDeleted(UUID id){
        PharmacyEntity pharmacyEntity = checkIsNotDeleted(id);
        if (Objects.isNull(pharmacyEntity)){
            throw new NotExistPharmacy("This pharmacy is not exist");
        }

        return entityToResponse(pharmacyEntity);
    }

    public PharmacyResponseDto getById(UUID id){
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFound("Pharmacy do not found"));
        return entityToResponse(pharmacyEntity);
    }

    public List<PharmacyResponseDto> getAll(int page, int pageSize){
        Pageable pageRequest = PageRequest.of(page, pageSize);
        List<PharmacyResponseDto> responseList = pharmacyRepository.findAll(pageRequest)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toList());

        return responseList;
    }

//    public List<PharmacyResponseDto> getCloser

    public PharmacyResponseDto updateAndNotDeleted(UUID id, PharmacyRequestDto requestDto){
        PharmacyEntity pharmacyEntity = checkIsNotDeleted(id);

        if (Objects.isNull(pharmacyEntity)){
            throw new NotExistPharmacy("This pharmacy is not exist");
        }
        modelMapper.map(requestDto, pharmacyEntity);

        ModelMapper modelMapper = new ModelMapper();
        PharmacyEntity pharmacy = modelMapper.map(requestDto, PharmacyEntity.class);
        return entityToResponse(pharmacyRepository.save(pharmacy));
    }


    private PharmacyEntity checkIsNotDeleted(UUID id){
        PharmacyEntity pharmacyEntity = pharmacyRepository.findById(id).orElseThrow(() -> new PharmacyNotFound("Pharmacy do not found"));

        if (pharmacyEntity.isActive()){
            return null;
        }
        return pharmacyEntity;
    }


    private PharmacyEntity requestToEntity(PharmacyRequestDto pharmacyRequestDto){
        return modelMapper.map(pharmacyRequestDto, PharmacyEntity.class);
    }

    private PharmacyResponseDto entityToResponse(PharmacyEntity pharmacyEntity){
       return modelMapper.map(pharmacyEntity, PharmacyResponseDto.class);
    }



}
