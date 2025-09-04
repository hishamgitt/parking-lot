package com.parkinglot.dto;

import java.util.List;

public class FloorSlotStatusResponse {
    private Long floorId;
    private Integer floorNumber;
    private List<SlotStatusResponse> slots;
    public FloorSlotStatusResponse() {}

    public FloorSlotStatusResponse(Long floorId, Integer floorNumber, List<SlotStatusResponse> slots) {
        this.floorId = floorId;
        this.floorNumber = floorNumber;
        this.slots = slots;
    }
    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<SlotStatusResponse> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotStatusResponse> slots) {
        this.slots = slots;
    }
}
