package com.parkinglot.service;

import com.parkinglot.dto.FloorRequest;
import com.parkinglot.dto.SlotRequest;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.repository.FloorRepository;
import com.parkinglot.repository.ParkingSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ParkingService {

    private final FloorRepository floorRepository;
    private final ParkingSlotRepository slotRepository;

    public ParkingService(FloorRepository floorRepository,
                          ParkingSlotRepository slotRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
    }

    public Floor createFloor(FloorRequest request) {
        if (floorRepository.findByFloorNumber(request.getFloorNumber()).isPresent()) {
            throw new IllegalArgumentException("Floor already exists");
        }

        Floor floor = new Floor();
        floor.setFloorNumber(request.getFloorNumber());
        return floorRepository.save(floor);
    }

    public ParkingSlot createSlot(SlotRequest request) {
        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        if (slotRepository.existsByFloorIdAndSlotNumber(
                request.getFloorId(), request.getSlotNumber())) {
            throw new IllegalArgumentException("Slot already exists on this floor");
        }

        ParkingSlot slot = new ParkingSlot();
        slot.setFloor(floor);
        slot.setSlotNumber(request.getSlotNumber());
        slot.setVehicleType(request.getVehicleType());

        return slotRepository.save(slot);
    }
}
