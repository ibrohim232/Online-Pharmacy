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
    @JoinColumn(name = "medicine_id")
    private MedicineEntity medicine;
    private int count;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderBucket orderBucket;

}
