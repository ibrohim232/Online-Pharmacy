package com.example.onlinemedicine.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
    {
        this.isActive = true;
    }
    @Id
    @GeneratedValue
    private UUID id;
    @CreationTimestamp
    protected LocalDateTime createdData;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

}
