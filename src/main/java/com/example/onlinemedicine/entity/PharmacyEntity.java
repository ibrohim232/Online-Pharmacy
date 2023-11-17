package com.example.onlinemedicine.entity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "pharmacy")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyEntity extends BaseEntity{


    private UUID ownerId;
    @Column(nullable = false)
    private String pharmacyName;
    @ElementCollection
    private Set<UUID> medicineId;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Location.class)
    private Location location;
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Contact.class)
    private Contact contact;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime openingTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime closingTime;
}
