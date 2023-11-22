package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import com.example.onlinemedicine.dto.order.response.OrderProductResponseDto;
import com.example.onlinemedicine.dto.payment.PaymentDto;
import com.example.onlinemedicine.dto.payment.PaymentResponseDto;
import com.example.onlinemedicine.entity.OrderBucket;
import com.example.onlinemedicine.entity.Payment;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.OrderRepository;
import com.example.onlinemedicine.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    public void save(PaymentDto paymentDto){
        OrderBucket order = orderRepository.findById(paymentDto.getOrderBucket())
                .orElseThrow(() -> new DataNotFoundException("Order buckets not found"));

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setOrderBucket(order);
        paymentRepository.save(payment);
    }
    public List<PaymentResponseDto> getUserPayments(UUID userId){
        List<Payment> userPayments = paymentRepository.findByOrderBucketOwnerId(userId);
      return   userPayments.stream()
                .map(
                        this::mapEntityToResponse
                ).toList();
    }
    public PaymentResponseDto findById(UUID paymentId){
        return mapEntityToResponse(paymentRepository.findById(paymentId)
                .orElseThrow(() -> new DataNotFoundException("Payment not found by id")));
    }
    public List<PaymentResponseDto> getAll(Integer page, Integer size){
        return paymentRepository.findAll(PageRequest.of(page,size))
                .get()
                .map(this::mapEntityToResponse)
                .toList();
    }
    private PaymentResponseDto mapEntityToResponse(Payment payment){
        OrderBucket orderBucket = payment.getOrderBucket();
        OrderBucketResponseDto response = new OrderBucketResponseDto(orderBucket);
        response.setOrderProductResponseDtos(orderBucket.getOrderProducts().stream().map(OrderProductResponseDto::new).toList());
        return new PaymentResponseDto(response,payment.getCardNumber(),payment.getPrice(),payment.getCreated());
    }
}
