package com.example.onlinemedicine.dto.payment;

import com.example.onlinemedicine.dto.base.BaseDto;
import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {
    private OrderBucketResponseDto orderBucket;
    private String cardNumber;
    private BigDecimal price;
    private LocalDateTime paymentData;
}
