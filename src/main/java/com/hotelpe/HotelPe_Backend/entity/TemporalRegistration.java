package com.hotelpe.HotelPe_Backend.entity;

import com.hotelpe.HotelPe_Backend.dto.UserDTO;
import org.springframework.data.annotation.Id;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TemporalRegistration{
    @Id
    public Integer id;
    private String asunto;
    private UserDTO usuario;
}