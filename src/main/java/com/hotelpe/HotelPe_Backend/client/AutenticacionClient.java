package com.hotelpe.HotelPe_Backend.client;

import com.hotelpe.HotelPe_Backend.config.FeignClientConfig;
import com.hotelpe.HotelPe_Backend.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "autenticacion", url = "http://localhost:8080/auth", configuration = FeignClientConfig.class)
public interface AutenticacionClient {

    @PostMapping("/register")
    ResponseEntity<RegisterResponseDto> register(@RequestBody UserDTO userDTO);
    @PostMapping("/verifyRegistration")
    ResponseEntity<VerifyRegistrationResponseDto> verifyRegistration(@RequestBody VerifyRegistrationDto verifyRegistrationDto);
    @PostMapping("/login")
    ResponseEntity<VerifyRegistrationResponseDto> login(@RequestBody LoginRequest loginRequest);
}
