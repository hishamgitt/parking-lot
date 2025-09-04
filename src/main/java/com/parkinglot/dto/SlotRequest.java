package com.parkinglot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SlotRequest {
    @NotNull(message = "Floor ID is required")
    private Long floorId;

    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Vehicle type is required")
    private VehicleTypeRequest vehicleType;

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public VehicleTypeRequest getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeRequest vehicleType) {
        this.vehicleType = vehicleType;
    }
}