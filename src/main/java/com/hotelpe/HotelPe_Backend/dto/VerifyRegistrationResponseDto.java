package com.hotelpe.HotelPe_Backend.dto;

import lombok.Data;

@Data
public class VerifyRegistrationResponseDto {
    private int statusCode;            // HTTP status code
    private String message;            // Response message
    private String token;              // JWT token
    private String role;               // User role
    private String expirationTime;     // Token expiration time
}

