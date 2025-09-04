package com.parkinglot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private String vehicleNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalCost;
    private SlotResponse slot;

    public ReservationResponse(Long id, String vehicleNumber,
                               LocalDateTime startTime, LocalDateTime endTime,
                               BigDecimal totalCost, SlotResponse slot) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
        this.slot = slot;
    }

    public Long getId() {
        return id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public SlotResponse getSlot() {
        return slot;
    }
}
