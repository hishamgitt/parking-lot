package com.parkinglot.repository;

import com.parkinglot.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByFloorId(Long floorId);
    boolean existsByFloorIdAndSlotNumber(Long floorId, String slotNumber);
}