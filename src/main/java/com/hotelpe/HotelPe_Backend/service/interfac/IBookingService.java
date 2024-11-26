package com.hotelpe.HotelPe_Backend.service.interfac;

import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.entity.Booking;

public interface IBookingService {

    Response saveBooking(int roomId, int userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(int bookingId);
}
