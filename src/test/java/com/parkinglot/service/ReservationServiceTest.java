package com.parkinglot.service;

import com.parkinglot.dto.ReservationRequest;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Reservation;
import com.parkinglot.model.VehicleType;
import com.parkinglot.repository.ParkingSlotRepository;
import com.parkinglot.repository.ReservationRepository;
import com.parkinglot.repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSlotRepository slotRepository;

    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private ParkingSlot testSlot;
    private VehicleType vehicleType;
    private ReservationRequest validRequest;

    @BeforeEach
    void setUp() {
        vehicleType = new VehicleType();
        vehicleType.setId(1L);
        vehicleType.setName("FOUR_WHEELER");
        vehicleType.setHourlyRate(30);

        testSlot = new ParkingSlot();
        testSlot.setId(1L);
        testSlot.setVehicleType(vehicleType);

        validRequest = new ReservationRequest();
        validRequest.setSlotId(1L);
        validRequest.setVehicleNumber("KA05MH1234");
        validRequest.setStartTime(LocalDateTime.now().plusHours(1));
        validRequest.setEndTime(LocalDateTime.now().plusHours(3));
    }

    @Test
    void createReservation_ValidRequest_ReturnsReservation() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findOverlappingReservations(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        Reservation result = reservationService.createReservation(validRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("KA05MH1234", result.getVehicleNumber());
        assertEquals(60, result.getTotalCost().intValue());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_SlotNotFound_ThrowsException() {
        when(slotRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(validRequest);
        });
    }

    @Test
    void createReservation_OverlappingReservation_ThrowsException() {
        when(slotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findOverlappingReservations(anyLong(), any(), any()))
                .thenReturn(List.of(new Reservation()));

        assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(validRequest);
        });
    }

    @Test
    void createReservation_StartTimeAfterEndTime_ThrowsException() {
        validRequest.setStartTime(LocalDateTime.now().plusHours(3));
        validRequest.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(validRequest);
        });
    }

    @Test
    void createReservation_Exceeds24Hours_ThrowsException() {
        validRequest.setEndTime(validRequest.getStartTime().plusHours(25));

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(validRequest);
        });
    }

    @Test
    void calculateCost_PartialHour_RoundsUp() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(90);

        BigDecimal cost = reservationService.calculateCost(startTime, endTime, vehicleType);

        assertEquals(60, cost.intValue());
    }

    @Test
    void getAvailableSlots_ValidRequest_ReturnsSlots() {
        Pageable pageable = PageRequest.of(0, 10);
        when(vehicleTypeRepository.findByName("FOUR_WHEELER"))
                .thenReturn(Optional.of(vehicleType));
        when(reservationRepository.findAvailableSlots(eq(vehicleType), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(testSlot)));

        Page<ParkingSlot> result = reservationService.getAvailableSlots(
                "FOUR_WHEELER",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                pageable
        );

        assertEquals(1, result.getContent().size());
        assertEquals(testSlot, result.getContent().get(0));
    }

    @Test
    void getReservation_Exists_ReturnsReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservation(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getReservation_NotExists_ThrowsException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservation(1L);
        });
    }

    @Test
    void cancelReservation_Exists_DeletesReservation() {
        when(reservationRepository.existsById(1L)).thenReturn(true);

        reservationService.cancelReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void cancelReservation_NotExists_ThrowsException() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.cancelReservation(1L);
        });
    }
}