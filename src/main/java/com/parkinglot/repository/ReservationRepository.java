package com.parkinglot.repository;


import com.parkinglot.dto.SlotStatusResponse;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Reservation;
import com.parkinglot.model.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.slot.id = :slotId " +
            "AND ((r.startTime <= :endTime AND r.endTime >= :startTime))")
    List<Reservation> findOverlappingReservations(
            @Param("slotId") Long slotId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT s FROM ParkingSlot s WHERE s.vehicleType = :vehicleType " +
            "AND s.id NOT IN (" +
            "SELECT r.slot.id FROM Reservation r WHERE " +
            "(r.startTime <= :endTime AND r.endTime >= :startTime))")
    Page<ParkingSlot> findAvailableSlots(
            @Param("vehicleType") VehicleType vehicleType,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable
    );

    boolean existsBySlotId(Long slotId);
}