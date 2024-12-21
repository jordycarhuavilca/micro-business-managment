package com.hotelpe.HotelPe_Backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    private int id;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private String role;
    @JsonIgnore
    private List<BookingDTO> bookings = new ArrayList<>();

}
