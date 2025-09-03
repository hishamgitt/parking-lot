package com.parkinglot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkinglot.controller.ParkingController;
import com.parkinglot.dto.FloorRequest;
import com.parkinglot.dto.ReservationRequest;
import com.parkinglot.dto.SlotRequest;
import com.parkinglot.model.Floor;
import com.parkinglot.model.ParkingSlot;
import com.parkinglot.model.Reservation;
import com.parkinglot.model.VehicleType;
import com.parkinglot.service.ParkingService;
import com.parkinglot.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ParkingController.class)
class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Floor testFloor;
    private ParkingSlot testSlot;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        testFloor = new Floor();
        testFloor.setId(1L);
        testFloor.setFloorNumber(1);

        testSlot = new ParkingSlot();
        testSlot.setId(1L);
        testSlot.setSlotNumber("A1");
        testSlot.setVehicleType(VehicleType.FOUR_WHEELER);
        testSlot.setFloor(testFloor);

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setSlot(testSlot);
        testReservation.setVehicleNumber("KA05MH1234");
        testReservation.setStartTime(LocalDateTime.now().plusHours(1));
        testReservation.setEndTime(LocalDateTime.now().plusHours(3));
        testReservation.setTotalCost(BigDecimal.valueOf(60));
    }

    @Test
    void createFloor_ValidRequest_ReturnsCreated() throws Exception {
        FloorRequest request = new FloorRequest();
        request.setFloorNumber(1);

        when(parkingService.createFloor(any(FloorRequest.class))).thenReturn(testFloor);
        mockMvc.perform(post("/api/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.floorNumber").value(1));
    }

    @Test
    void createFloor_InvalidRequest_ReturnsBadRequest() throws Exception {
        FloorRequest request = new FloorRequest();
        mockMvc.perform(post("/api/floors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSlot_ValidRequest_ReturnsCreated() throws Exception {
        SlotRequest request = new SlotRequest();
        request.setFloorId(1L);
        request.setSlotNumber("A1");
        request.setVehicleType(VehicleType.FOUR_WHEELER);
        when(parkingService.createSlot(any(SlotRequest.class))).thenReturn(testSlot);
        mockMvc.perform(post("/api/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.slotNumber").value("A1"));
    }

    @Test
    void reserveSlot_ValidRequest_ReturnsCreated() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setSlotId(1L);
        request.setVehicleNumber("KA05MH1234");
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(3));
        when(reservationService.createReservation(any(ReservationRequest.class))).thenReturn(testReservation);
        mockMvc.perform(post("/api/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("KA05MH1234"));
    }

    @Test
    void reserveSlot_InvalidVehicleNumber_ReturnsBadRequest() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setSlotId(1L);
        request.setVehicleNumber("INVALID"); // Invalid format
        request.setStartTime(LocalDateTime.now().plusHours(1));
        request.setEndTime(LocalDateTime.now().plusHours(3));
        mockMvc.perform(post("/api/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailability_ValidRequest_ReturnsOk() throws Exception {
        Page<ParkingSlot> page = new PageImpl<>(List.of(testSlot));
        when(reservationService.getAvailableSlots(any(), any(), any(), any())).thenReturn(page);
        mockMvc.perform(get("/api/availability")
                        .param("vehicleType", "FOUR_WHEELER")
                        .param("startTime", LocalDateTime.now().toString())
                        .param("endTime", LocalDateTime.now().plusHours(2).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].slotNumber").value("A1"));
    }

    @Test
    void getReservation_Exists_ReturnsOk() throws Exception {
        when(reservationService.getReservation(1L)).thenReturn(testReservation);
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vehicleNumber").value("KA05MH1234"));
    }

    @Test
    void cancelReservation_Exists_ReturnsNoContent() throws Exception {
        doNothing().when(reservationService).cancelReservation(1L);
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }
}
