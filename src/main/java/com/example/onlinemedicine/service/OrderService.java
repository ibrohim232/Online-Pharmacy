package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.order.request.OrderBucketRequestDto;
import com.example.onlinemedicine.dto.order.request.OrderProductRequestDto;
import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import com.example.onlinemedicine.dto.order.response.OrderProductResponseDto;
import com.example.onlinemedicine.entity.OrderBucket;
import com.example.onlinemedicine.entity.OrderProduct;
import com.example.onlinemedicine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper  modelMapper;

    public OrderBucketResponseDto save(OrderBucketRequestDto orderBucketRequestDto){
        if (orderBucketRequestDto!=null&&orderBucketRequestDto.getOrderProductRequestDtos()!=null){
            OrderBucket orderBucket = modelMapper.map(orderBucketRequestDto, OrderBucket.class);
            List<OrderProduct> orderProducts=new ArrayList<>();
            for (OrderProductRequestDto orderProductRequestDto : orderBucketRequestDto.getOrderProductRequestDtos()) {
                orderProducts.add(
                        modelMapper.map(orderProductRequestDto,OrderProduct.class)
                );
            }
            orderBucket.setOrderProducts(orderProducts);
            OrderBucket saved = orderRepository.save(orderBucket);
            OrderBucketResponseDto orderBucketResponseDto = modelMapper.map(saved, OrderBucketResponseDto.class);
            List<OrderProductResponseDto> orderProductResponseDtos=new ArrayList<>();
            for (OrderProduct orderProduct : saved.getOrderProducts()) {
                orderProductResponseDtos.add(
                        modelMapper.map(orderProduct, OrderProductResponseDto.class)
                );
            }
            orderBucketResponseDto.setOrderProductResponseDtos(orderProductResponseDtos);
            return orderBucketResponseDto;
        }
        throw new RuntimeException("Request is null");

    }

    public List<OrderBucketResponseDto> getAll() {
        List<OrderBucket> orderBuckets = orderRepository.findAll();
       return mapAll(orderBuckets);
    }

    public List<OrderBucketResponseDto> getAllById(UUID id) {
        List<OrderBucket> orderBuckets = orderRepository.findAllById(id).orElseThrow(() -> new RuntimeException("Orders not found by id"));
       return mapAll(orderBuckets);
    }

    private List<OrderBucketResponseDto> mapAll(List<OrderBucket> orderBuckets){
        List<OrderBucketResponseDto> orderBucketResponseDtos=new ArrayList<>();

        for (OrderBucket orderBucket : orderBuckets) {
            List<OrderProductResponseDto> orderProductResponseDtos=new ArrayList<>();
            OrderBucketResponseDto orderBucketResponseDto = modelMapper.map(orderBucket, OrderBucketResponseDto.class);

            for (OrderProduct orderProduct : orderBucket.getOrderProducts()) {
                orderProductResponseDtos.add(
                        modelMapper.map(orderProduct, OrderProductResponseDto.class)
                );
            }
            orderBucketResponseDto.setOrderProductResponseDtos(orderProductResponseDtos);
            orderBucketResponseDtos.add(orderBucketResponseDto);
        }
        return orderBucketResponseDtos;
    }

    public OrderBucketResponseDto delete(UUID id) {
        OrderBucket ordersNotFoundById = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Orders not found by id"));
        orderRepository.deleteById(id);
        return modelMapper.map(ordersNotFoundById, OrderBucketResponseDto.class);
    }
}
