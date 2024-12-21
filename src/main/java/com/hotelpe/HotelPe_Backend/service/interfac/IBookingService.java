package com.hotelpe.HotelPe_Backend.service.interfac;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelpe.HotelPe_Backend.dto.CardPaymentDTO;
import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.entity.Booking;

public interface IBookingService {

    Response  booking (CardPaymentDTO cardPaymentDTO)throws JsonProcessingException;

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(int bookingId);
}
