package com.example.onlinemedicine.dto.pharmacy.response;


import com.example.onlinemedicine.entity.Contact;
import com.example.onlinemedicine.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyResponseDto extends RepresentationModel<PharmacyResponseDto> {

    private UUID id;
    private String pharmacyName;
    private Set<UUID> medicineId;
    private Location location;
    private Contact contact;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;

}
