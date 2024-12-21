package com.hotelpe.HotelPe_Backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelpe.HotelPe_Backend.dto.CardPaymentDTO;
import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.service.interfac.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @PostMapping("/booking")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> saveBookings(@RequestBody CardPaymentDTO reservation) {
        try {
            Response response = bookingService.booking(reservation);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body(null);

        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings() {
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-by-confirmation-code/{confirmationCode}")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> cancelBooking(@PathVariable int bookingId) {
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
