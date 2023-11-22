package com.example.onlinemedicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderProduct extends BaseEntity {
    @ManyToOne
    private MedicineEntity medicine;
    private int count;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderBucket orderBucket;
}
