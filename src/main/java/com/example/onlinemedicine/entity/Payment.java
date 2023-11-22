package com.example.onlinemedicine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Payment extends BaseEntity {
    @OneToOne(fetch = FetchType.EAGER)
    private OrderBucket orderBucket;
    private String cardNumber;
    private BigDecimal price;
}
