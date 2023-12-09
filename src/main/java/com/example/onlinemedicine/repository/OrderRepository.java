package com.example.onlinemedicine.repository;


import com.example.onlinemedicine.entity.OrderBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderBucket, UUID> {
    Optional<List<OrderBucket>> findAllById(UUID id);
}
