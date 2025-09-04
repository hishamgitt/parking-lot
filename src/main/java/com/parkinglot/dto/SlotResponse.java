package com.parkinglot.dto;

public class SlotResponse {
    private Long id;
    private String slotNumber;
    private String vehicleTypeName;
    private Integer hourlyRate;
    private Integer floorNumber;

    public SlotResponse(Long id, String slotNumber, String vehicleTypeName,
                        Integer hourlyRate, Integer floorNumber) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.vehicleTypeName = vehicleTypeName;
        this.hourlyRate = hourlyRate;
        this.floorNumber = floorNumber;
    }

    public Long getId() {
        return id;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public Integer getHourlyRate() {
        return hourlyRate;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }
}
