package com.hotelpe.HotelPe_Backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotelpe.HotelPe_Backend.config.SqsMessageProducer;
import com.hotelpe.HotelPe_Backend.dto.BookingDTO;
import com.hotelpe.HotelPe_Backend.dto.CardPaymentDTO;
import com.hotelpe.HotelPe_Backend.dto.Response;
import com.hotelpe.HotelPe_Backend.entity.Booking;
import com.hotelpe.HotelPe_Backend.entity.Room;
import com.hotelpe.HotelPe_Backend.entity.User;
import com.hotelpe.HotelPe_Backend.exception.OurException;
import com.hotelpe.HotelPe_Backend.repo.BookingRepository;
import com.hotelpe.HotelPe_Backend.repo.RoomRepository;
import com.hotelpe.HotelPe_Backend.repo.UserRepository;
import com.hotelpe.HotelPe_Backend.service.interfac.IBookingService;
import com.hotelpe.HotelPe_Backend.service.interfac.IRoomService;
import com.hotelpe.HotelPe_Backend.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private IRoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SqsMessageProducer publisher;
    @Value("${spring.cloud.aws.sqs.endpoint.booking}")
    private String bookingQueueName;

    @Override
    public Response booking(CardPaymentDTO cardPaymentDTO) throws JsonProcessingException {
        log.info("booking.init " + cardPaymentDTO.toString());
        Response response = new Response();
        BookingDTO reservation = cardPaymentDTO.getAdditionalInfo().getItem();
        LocalDate checkInDate = LocalDate.parse(reservation.getCheckInDate());
        LocalDate checkOutDate = LocalDate.parse(reservation.getCheckInDate());

        if (checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException("Check in date must come after check out date");
        }
        Room room = roomRepository.findById((long) reservation.getRoom().getId()).orElseThrow(() -> new OurException("Room Not Found"));
        User user = userRepository.findById((long) reservation.getUser().getId()).orElseThrow(() -> new OurException("User Not Found"));

        List<Booking> existingBookings = room.getBookings();
        Booking booking = Utils.mapBookingDTOEntityToBooking(reservation);

        if (!roomIsAvailable(booking, existingBookings)) {
            throw new OurException("Room not Available for selected date range");
        }

        booking.setRoom(room);
        booking.setUser(user);
        String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
        booking.setBookingConfirmationCode(bookingConfirmationCode);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        log.info("booking.sent.bookingQueueName " + booking.toString());
        publisher.send(booking,bookingQueueName,headers);

        response.setStatusCode(200);
        response.setMessage("successful");

        return response;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {

        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new OurException("Booking Not Found"));
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Finding a booking: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response getAllBookings() {

        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Getting all bookings: " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response cancelBooking(int bookingId) {

        Response response = new Response();

        try {
            bookingRepository.findById((long) bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById((long) bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return response;
    }


    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
