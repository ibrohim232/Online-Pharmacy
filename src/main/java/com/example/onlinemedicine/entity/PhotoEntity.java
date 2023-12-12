package com.example.onlinemedicine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhotoEntity extends BaseEntity {
    private String type;
    private String filePath;
    private String name;
    @ManyToOne
    private MedicineEntity medicine;

}
