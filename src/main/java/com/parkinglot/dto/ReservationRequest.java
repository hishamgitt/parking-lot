package com.parkinglot.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ReservationRequest {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotBlank(message = "Vehicle number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$",
            message = "Vehicle number must be in format XX00XX0000")
    private String vehicleNumber;

    @FutureOrPresent(message = "Start time must be in the present or future")
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
