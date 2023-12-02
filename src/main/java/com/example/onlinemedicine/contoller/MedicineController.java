package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
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

    @GetMapping("/find-by-name-order-by-lower-price")
    public List<MedicineResponseDto> findByNameOrderByLowerPrice(@RequestParam String name) {
        return medicineService.findByNameOrderByLower(name);
    }

    @GetMapping("/find-by-name-order-by-higher-price")
    public List<MedicineResponseDto> findByNameOrderByHigherPrice(@RequestParam String name) {
        return medicineService.findByNameOrderByHigher(name);
    }

    @GetMapping("/find-by-name")
    public List<MedicineResponseDto> findByName(@RequestParam String name) {
        return medicineService.findByName(name);
    }
    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @GetMapping("/increase-medicine-count")
    public void increaseMedicineCount(@RequestParam UUID id) {
        medicineService.increaseOrDecreaseMedicineCount(id, true);
    }
    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @GetMapping("/decrease-medicine-count")
    public void decreaseMedicineCount(@RequestParam UUID id) {
        medicineService.increaseOrDecreaseMedicineCount(id, false);
    }
    @PreAuthorize("hasRole('SUPER_ADMIN') and hasAuthority('CHANGE_ROLE')")
    @GetMapping("/set-medicine-count")
    public void setMedicineCount(@RequestParam UUID id,@RequestParam int count) {
        medicineService.setMedicineCount(id,count);
    }


}
