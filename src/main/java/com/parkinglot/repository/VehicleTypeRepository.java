package com.parkinglot.repository;

import com.parkinglot.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
    Optional<VehicleType> findByName(String name);
    Optional<VehicleType> findByNameIgnoreCase(String name);
}