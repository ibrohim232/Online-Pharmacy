package com.example.onlinemedicine.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductResponseDto {
    private UUID medicineId;
    private int count;
    private Double price;
}
