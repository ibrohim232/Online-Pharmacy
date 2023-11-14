package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.service.MedicineService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/medicine"})
public class MedicineController {
    private final MedicineService medicineService;

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @GetMapping({"/get-all"})
    public List<MedicineResponseDto> getAll(@RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "1") int page) {
        return this.medicineService.getAll(page, size);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @PostMapping({"/create"})
    public MedicineResponseDto create(@RequestBody MedicineRequestDto medicineRequestDto) {
        return this.medicineService.create(medicineRequestDto);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @GetMapping({"/find-by-id"})
    public MedicineResponseDto findById(@RequestParam UUID id) {
        return this.medicineService.findById(id);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @DeleteMapping({"/delete-by-id"})
    public void deleteById(@RequestParam UUID id) {
        this.medicineService.deleteById(id);
    }

    public MedicineController(final MedicineService medicineService) {
        this.medicineService = medicineService;
    }
}
