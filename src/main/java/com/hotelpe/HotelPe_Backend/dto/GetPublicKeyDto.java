package com.hotelpe.HotelPe_Backend.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPublicKeyDto {
    private int statusCode;
    private String token;
}
