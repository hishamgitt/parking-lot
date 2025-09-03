package com.parkinglot.controller;

import com.parkinglot.dto.FloorRequest;
import com.parkinglot.dto.ReservationRequest;
import com.parkinglot.dto.SlotRequest;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Reservation;
import com.parkinglot.model.VehicleType;
import com.parkinglot.service.ParkingService;
import com.parkinglot.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@Validated
public class ParkingController {

    private final ParkingService parkingService;
    private final ReservationService reservationService;

    public ParkingController(ParkingService parkingService,
                             ReservationService reservationService) {
        this.parkingService = parkingService;
        this.reservationService = reservationService;
    }

    @PostMapping("/floors")
    public ResponseEntity<Floor> createFloor(@Valid @RequestBody FloorRequest request) {
        Floor floor = parkingService.createFloor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(floor);
    }

    @PostMapping("/slots")
    public ResponseEntity<ParkingSlot> createSlot(@Valid @RequestBody SlotRequest request) {
        ParkingSlot slot = parkingService.createSlot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(slot);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveSlot(@Valid @RequestBody ReservationRequest request) {
        Reservation reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/availability")
    public ResponseEntity<Page<ParkingSlot>> getAvailability(
            @RequestParam VehicleType vehicleType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ParkingSlot> availableSlots = reservationService
                .getAvailableSlots(vehicleType, startTime, endTime, pageable);

        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
