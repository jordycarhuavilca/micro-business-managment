package com.hotelpe.HotelPe_Backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO implements Serializable {
    private int id;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private String checkInDate;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private String checkOutDate;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private UserDTO user;
    private RoomDTO room;
}
