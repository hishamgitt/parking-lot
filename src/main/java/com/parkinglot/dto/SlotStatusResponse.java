package com.parkinglot.dto;

import java.time.LocalDateTime;

public class SlotStatusResponse {
    private Long slotId;
    private String slotNumber;
    private String vehicleType;
    private Integer hourlyRate;
    private boolean isReserved;
    private LocalDateTime reservedFrom;
    private LocalDateTime reservedUntil;
    private String reservedVehicleNumber;
    public SlotStatusResponse() {}

    public SlotStatusResponse(Long slotId, String slotNumber, String vehicleType, Integer hourlyRate,
                              boolean isReserved, LocalDateTime reservedFrom, LocalDateTime reservedUntil,
                              String reservedVehicleNumber) {
        this.slotId = slotId;
        this.slotNumber = slotNumber;
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
        this.isReserved = isReserved;
        this.reservedFrom = reservedFrom;
        this.reservedUntil = reservedUntil;
        this.reservedVehicleNumber = reservedVehicleNumber;
    }
    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Integer hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public LocalDateTime getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(LocalDateTime reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String getReservedVehicleNumber() {
        return reservedVehicleNumber;
    }

    public void setReservedVehicleNumber(String reservedVehicleNumber) {
        this.reservedVehicleNumber = reservedVehicleNumber;
    }
}
