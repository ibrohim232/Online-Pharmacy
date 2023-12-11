package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.midicine.MedicineRequestDto;
import com.example.onlinemedicine.dto.midicine.MedicineResponseDto;
import com.example.onlinemedicine.service.MedicineService;
import com.example.onlinemedicine.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;
    private final PhotoService photoService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping({"/get-all"})
    public List<MedicineResponseDto> getAll(@RequestParam(defaultValue = "1") int size, @RequestParam(defaultValue = "1") int page) {
        return this.medicineService.getAll(page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping({"/create"})
    public MedicineResponseDto create(@RequestPart("dto") MedicineRequestDto medicineRequestDto, @RequestPart("file") MultipartFile file) throws IOException {
        return this.medicineService.create(medicineRequestDto, file);
    }


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

    @PreAuthorize("hasRole('SUPER_USER')")
    @GetMapping("/increase-medicine-count")
    public void increaseMedicineCount(@RequestParam UUID id) {
        medicineService.increaseOrDecreaseMedicineCount(id, true);
    }

    @PreAuthorize("hasRole('SUPER_USER')")
    @GetMapping("/decrease-medicine-count")
    public void decreaseMedicineCount(@RequestParam UUID id) {
        medicineService.increaseOrDecreaseMedicineCount(id, false);
    }

    @PreAuthorize("hasRole('SUPER_USER')")
    @GetMapping("/set-medicine-count")
    public void setMedicineCount(@RequestParam UUID id, @RequestParam int count) {
        medicineService.setMedicineCount(id, count);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = photoService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }


}
