package com.example.onlinemedicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_bucket")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderBucket extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity owner;
    private Double price;
    private Double deliveryPrice;
    @OneToMany(mappedBy = "orderBucket",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderProduct> orderProducts;
}
