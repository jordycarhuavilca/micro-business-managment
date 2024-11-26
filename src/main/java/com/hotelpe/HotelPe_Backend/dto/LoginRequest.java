package com.hotelpe.HotelPe_Backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Correo Requerido")
    private String email;
    @NotBlank(message = "Contraseña Requerida")
    private String password;
}
