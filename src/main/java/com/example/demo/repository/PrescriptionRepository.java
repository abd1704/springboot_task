package com.example.demo.repository;


import com.example.demo.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    // You can add additional query methods here if needed
}
