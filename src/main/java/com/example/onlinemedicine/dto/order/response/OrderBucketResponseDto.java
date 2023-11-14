package com.example.onlinemedicine.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBucketResponseDto {
    private UUID id;
    private UUID ownerId;
    private Double price;
    private Double deliveryPrice;
    private List<OrderProductResponseDto> orderProductResponseDtos;
}
