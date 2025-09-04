package com.parkinglot.service;

import com.parkinglot.dto.*;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.VehicleType;
import com.parkinglot.repository.FloorRepository;
import com.parkinglot.repository.ParkingSlotRepository;
import com.parkinglot.repository.ReservationRepository;
import com.parkinglot.repository.VehicleTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParkingService {

    private final FloorRepository floorRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final ReservationRepository reservationRepository;

    public ParkingService(FloorRepository floorRepository,
                          ParkingSlotRepository slotRepository, VehicleTypeRepository vehicleTypeRepository, ReservationRepository reservationRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.reservationRepository = reservationRepository;
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

        VehicleType vehicleType = vehicleTypeRepository.findByName(request.getVehicleType().getName())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type '" + request.getVehicleType().getName() + "' not found"));
        if (slotRepository.existsByFloorIdAndSlotNumber(
                request.getFloorId(), request.getSlotNumber())) {
            throw new IllegalArgumentException("Slot already exists on this floor");
        }

        ParkingSlot slot = new ParkingSlot();
        slot.setFloor(floor);
        slot.setSlotNumber(request.getSlotNumber());
        slot.setVehicleType(vehicleType);

        return slotRepository.save(slot);
    }
    public List<ParkingSlot> getAllParkingSlots() {
        List<ParkingSlot> parkingSlots =   slotRepository.findAll();
        return parkingSlots;
    }
}
