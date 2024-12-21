package com.hotelpe.HotelPe_Backend.controller;

import com.hotelpe.HotelPe_Backend.dto.LoginRequest;
import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.dto.UserDTO;
import com.hotelpe.HotelPe_Backend.dto.VerifyRegistrationDto;
import com.hotelpe.HotelPe_Backend.entity.User;
import com.hotelpe.HotelPe_Backend.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserDTO user) {
        Response response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/verifyRegistration")
    public Response verifyRegistration(@RequestBody VerifyRegistrationDto verifyRegistrationDto) {
        return this.userService.verifyRegistration(verifyRegistrationDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
