package com.parkinglot.model;

public enum VehicleType {
    TWO_WHEELER(20),
    FOUR_WHEELER(30);

    private final int hourlyRate;

    VehicleType(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }
}
