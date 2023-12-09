package com.example.onlinemedicine.dto.order.response;

import com.example.onlinemedicine.entity.OrderProduct;
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
    public OrderProductResponseDto(OrderProduct orderProduct){
        this.medicineId=orderProduct.getMedicine().getId();
        this.count=orderProduct.getCount();
        this.price=orderProduct.getPrice();
    }
}
