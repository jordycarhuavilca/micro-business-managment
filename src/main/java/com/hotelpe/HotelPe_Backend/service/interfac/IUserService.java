package com.hotelpe.HotelPe_Backend.service.interfac;

import com.hotelpe.HotelPe_Backend.dto.LoginRequest;
import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.dto.UserDTO;
import com.hotelpe.HotelPe_Backend.dto.VerifyRegistrationDto;

public interface IUserService {
    Response register(UserDTO user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

    Response verifyRegistration(VerifyRegistrationDto verifyRegistrationDto);
}
