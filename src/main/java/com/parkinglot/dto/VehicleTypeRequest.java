package com.parkinglot.dto;

import jakarta.validation.constraints.NotBlank;

public class VehicleTypeRequest {
    @NotBlank(message = "Vehicle type name is required")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
