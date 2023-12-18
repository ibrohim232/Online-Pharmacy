package com.example.onlinemedicine.service;

import com.example.onlinemedicine.dto.order.request.OrderBucketRequestDto;
import com.example.onlinemedicine.dto.order.request.OrderProductRequestDto;
import com.example.onlinemedicine.dto.order.response.OrderBucketResponseDto;
import com.example.onlinemedicine.dto.order.response.OrderProductResponseDto;
import com.example.onlinemedicine.entity.*;
import com.example.onlinemedicine.exception.DataNotFoundException;
import com.example.onlinemedicine.repository.MedicineRepository;
import com.example.onlinemedicine.repository.OrderRepository;
import com.example.onlinemedicine.repository.PharmacyRepository;
import com.example.onlinemedicine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final ModelMapper  modelMapper;

    public List<OrderBucketResponseDto> save(OrderBucketRequestDto orderBucketRequestDto,UUID userId){
        UserEntity owner = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Owner not found while adding new order"));
        if (orderBucketRequestDto.getOrderProductRequestDtos() != null){
            List<OrderProductRequestDto> orderProductRequestDtos = orderBucketRequestDto.getOrderProductRequestDtos();

            Map<UUID, List<OrderProductRequestDto>> ordersForPharmacies = orderProductRequestDtos.stream().collect(Collectors.groupingBy(OrderProductRequestDto::getPharmacyId));
            List<OrderBucketResponseDto> orderBucketResponseDtos=new ArrayList<>();
            for (Map.Entry<UUID, List<OrderProductRequestDto>> order : ordersForPharmacies.entrySet()) {
                double overallPrice =  0;
                double price=0;
                PharmacyEntity pharmacyEntity = pharmacyRepository.findById(order.getKey())
                        .orElseThrow(() -> new DataNotFoundException("Pharmacy not found while setting order"));

                OrderBucket orderBucket = modelMapper.map(orderBucketRequestDto, OrderBucket.class);
                List<OrderProduct> orderProducts=new ArrayList<>();
                for (OrderProductRequestDto orderProductRequestDto : orderBucketRequestDto.getOrderProductRequestDtos()) {
                    OrderProduct orderProduct = modelMapper.map(orderProductRequestDto, OrderProduct.class);
                    MedicineEntity medicine = medicineRepository.findById(orderProductRequestDto.getMedicineId())
                            .orElseThrow(() -> new DataNotFoundException("Medicine not found"));
                    orderProduct.setMedicine(medicine);
                    orderProduct.setPrice(medicine.getPrice());
                    orderProducts.add(orderProduct);
                    orderProduct.setOrderBucket(orderBucket);
                    overallPrice=(overallPrice+medicine.getPrice());
                    price=price+medicine.getPrice();
                }
                orderBucket.setOrderProducts(orderProducts);
                orderBucket.setPrice(price);
                price=0;
                pharmacyEntity.getMyOrders().add(orderBucket);
                orderBucket.setPharmacyEntity(pharmacyEntity);
                orderBucket.setOwner(owner);
                OrderBucket saved = orderRepository.save(orderBucket);

                OrderBucketResponseDto orderBucketResponseDto = modelMapper.map(saved, OrderBucketResponseDto.class);
                orderBucketResponseDto.setOwner(owner.getFullName());
                orderBucketResponseDto.setPrice(overallPrice);
                List<OrderProductResponseDto> orderProductResponseDtos=new ArrayList<>();
                for (OrderProduct orderProduct : saved.getOrderProducts()) {
                    MedicineEntity medicine = medicineRepository.findById(orderProduct.getMedicine().getId())
                            .orElseThrow(() -> new DataNotFoundException("Medicine not found while decrementing"));
                    medicine.setCount(medicine.getCount()-orderProduct.getCount());
                    medicineRepository.save(medicine);
                    OrderProductResponseDto orderProductResponseDto = modelMapper.map(orderProduct, OrderProductResponseDto.class);
                    orderProductResponseDto.setMedicineId(medicine.getId());
                    orderProductResponseDtos.add(orderProductResponseDto);
                }
                orderBucketResponseDto.setOrderProductResponseDtos(orderProductResponseDtos);
                orderBucketResponseDtos.add(orderBucketResponseDto);
            }

            return orderBucketResponseDtos;
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

//    public OrderBucketResponseDto getByBucketId(UUID bucketId) {
//        orderRepository.findById(bucketId);
//    }
}
