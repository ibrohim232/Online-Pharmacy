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
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "pharmacy")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyEntity extends BaseEntity{

    @ManyToOne
    private UserEntity owner;
    @Column(nullable = false)
    private String pharmacyName;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Location.class, cascade = CascadeType.ALL)
    private Location location;
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Contact.class, cascade = CascadeType.ALL)
    private Contact contact;
    @OneToMany
    private List<OrderBucket> myOrders;
    @OneToMany(fetch =FetchType.EAGER,cascade = CascadeType.ALL)
    private List<MedicineEntity> medicines;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime openingTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime closingTime;
}
