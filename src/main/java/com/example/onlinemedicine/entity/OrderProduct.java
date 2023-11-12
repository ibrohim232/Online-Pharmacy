package com.example.onlinemedicine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private UUID medicineId;
    private int count;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderBucket orderBucket;
}
