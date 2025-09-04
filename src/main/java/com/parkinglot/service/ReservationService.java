package com.parkinglot.service;

import com.parkinglot.dto.ReservationRequest;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Reservation;
import com.parkinglot.model.VehicleType;
import com.parkinglot.repository.FloorRepository;
import com.parkinglot.repository.ParkingSlotRepository;
import com.parkinglot.repository.ReservationRepository;
import com.parkinglot.repository.VehicleTypeRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private static final Logger logger = LogManager.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final FloorRepository floorRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ParkingSlotRepository slotRepository, VehicleTypeRepository vehicleTypeRepository, FloorRepository floorRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.floorRepository = floorRepository;
    }

    public Reservation createReservation(ReservationRequest request) {
            validateReservationTime(request.getStartTime(), request.getEndTime());

            ParkingSlot slot = slotRepository.findById(request.getSlotId())
                    .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

            checkForOverlappingReservations(request.getSlotId(),
                    request.getStartTime(), request.getEndTime());

            BigDecimal cost = calculateCost(request.getStartTime(),
                    request.getEndTime(), slot.getVehicleType());

            Reservation reservation = new Reservation();
            reservation.setSlot(slot);
            reservation.setVehicleNumber(request.getVehicleNumber());
            reservation.setStartTime(request.getStartTime());
            reservation.setEndTime(request.getEndTime());
            reservation.setTotalCost(cost);
            return reservationRepository.save(reservation);
    }

    public void validateReservationTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        long hours = Duration.between(startTime, endTime).toHours();
        if (hours > 24) {
            throw new IllegalArgumentException("Reservation cannot exceed 24 hours");
        }
    }

    public void validateReservationTimeAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    public void checkForOverlappingReservations(Long slotId,
                                                 LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> overlapping = reservationRepository
                .findOverlappingReservations(slotId, startTime, endTime);

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Slot is already reserved for the given time range");
        }
    }

    public BigDecimal calculateCost(LocalDateTime startTime,
                                     LocalDateTime endTime, VehicleType vehicleType) {
        long minutes = Duration.between(startTime, endTime).toMinutes();
        long hours = (minutes + 59) / 60;

        return BigDecimal.valueOf(hours * vehicleType.getHourlyRate());
    }

    public Page<ParkingSlot> getAvailableSlots(String vehicleTypeName,
                                               LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        validateReservationTimeAvailable(startTime, endTime);
        VehicleType vehicleType = vehicleTypeRepository.findByName(vehicleTypeName)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle type '" + vehicleTypeName + "' not found"));
        return reservationRepository.findAvailableSlots(
                vehicleType, startTime, endTime, pageable);
    }

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    public void cancelReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found");
        }
        reservationRepository.deleteById(id);
    }
}
