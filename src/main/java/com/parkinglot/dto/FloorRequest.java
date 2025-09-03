package com.parkinglot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FloorRequest {
    @NotNull(message = "Floor number is required")
    @Min(value = 1, message = "Floor number must be positive")
    private Integer floorNumber;

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }
}
