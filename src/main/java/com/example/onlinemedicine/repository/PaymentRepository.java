package com.example.onlinemedicine.repository;

import com.example.onlinemedicine.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrderBucketOwnerId(UUID ownerId);
}
