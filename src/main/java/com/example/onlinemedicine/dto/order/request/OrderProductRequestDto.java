package com.example.onlinemedicine.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductRequestDto {
    private UUID medicineId;
    private UUID pharmacyId;
    private int count;
    private Double price;
}
