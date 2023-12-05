package com.example.onlinemedicine.dto.pharmacy.request;


import com.example.onlinemedicine.dto.contact.request.ContactRequestDto;
import com.example.onlinemedicine.dto.location.LocationRequestDto;
import com.example.onlinemedicine.entity.Contact;
import com.example.onlinemedicine.entity.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyRequestDto {

    @NotBlank
    private UUID ownerId;
    @NotBlank
    private String pharmacyName;
    private Set<UUID> medicineId;
    private LocationRequestDto location;
    private ContactRequestDto contact;
    @NotBlank
    private LocalDateTime openingTime;
    @NotBlank
    private LocalDateTime closingTime;

}
