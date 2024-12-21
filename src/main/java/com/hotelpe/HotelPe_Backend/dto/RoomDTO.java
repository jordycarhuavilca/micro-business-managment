package com.hotelpe.HotelPe_Backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomDTO implements Serializable {
    private int id;
    private String roomType;
    private Double roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    @JsonIgnore
    private List<BookingDTO> bookings;
}
