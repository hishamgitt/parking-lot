package com.parkinglot;

import com.parkinglot.dto.FloorRequest;
import com.parkinglot.dto.SlotRequest;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.VehicleType;
import com.parkinglot.repository.FloorRepository;
import com.parkinglot.repository.ParkingSlotRepository;
import com.parkinglot.service.ParkingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private FloorRepository floorRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private ParkingService parkingService;

    private FloorRequest floorRequest;
    private SlotRequest slotRequest;

    @BeforeEach
    void setUp() {
        floorRequest = new FloorRequest();
        floorRequest.setFloorNumber(1);

        slotRequest = new SlotRequest();
        slotRequest.setFloorId(1L);
        slotRequest.setSlotNumber("A1");
        slotRequest.setVehicleType(VehicleType.FOUR_WHEELER);
    }

    @Test
    void createFloor_ValidRequest_ReturnsFloor() {
        when(floorRepository.findByFloorNumber(1)).thenReturn(Optional.empty());
        when(floorRepository.save(any(Floor.class))).thenAnswer(invocation -> {
            Floor floor = invocation.getArgument(0);
            floor.setId(1L);
            return floor;
        });
        Floor result = parkingService.createFloor(floorRequest);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getFloorNumber());
        verify(floorRepository, times(1)).save(any(Floor.class));
    }

    @Test
    void createFloor_AlreadyExists_ThrowsException() {
        Floor existingFloor = new Floor();
        existingFloor.setId(1L);
        when(floorRepository.findByFloorNumber(1)).thenReturn(Optional.of(existingFloor));
        assertThrows(IllegalArgumentException.class, () -> {
            parkingService.createFloor(floorRequest);
        });
    }

    @Test
    void createSlot_ValidRequest_ReturnsSlot() {
        Floor floor = new Floor();
        floor.setId(1L);
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(slotRepository.existsByFloorIdAndSlotNumber(1L, "A1")).thenReturn(false);
        when(slotRepository.save(any(ParkingSlot.class))).thenAnswer(invocation -> {
            ParkingSlot slot = invocation.getArgument(0);
            slot.setId(1L);
            return slot;
        });
        ParkingSlot result = parkingService.createSlot(slotRequest);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("A1", result.getSlotNumber());
        assertEquals(VehicleType.FOUR_WHEELER, result.getVehicleType());
        verify(slotRepository, times(1)).save(any(ParkingSlot.class));
    }

    @Test
    void createSlot_FloorNotFound_ThrowsException() {
        when(floorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            parkingService.createSlot(slotRequest);
        });
    }

    @Test
    void createSlot_SlotAlreadyExists_ThrowsException() {
        Floor floor = new Floor();
        floor.setId(1L);
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        when(slotRepository.existsByFloorIdAndSlotNumber(1L, "A1")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> {
            parkingService.createSlot(slotRequest);
        });
    }
}