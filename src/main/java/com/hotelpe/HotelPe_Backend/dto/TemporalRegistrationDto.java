package com.hotelpe.HotelPe_Backend.dto;

import com.hotelpe.HotelPe_Backend.entity.User;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TemporalRegistrationDto{
    protected Integer id;
    private String asunto;
    private User usuario;
}
