package com.hotelpe.HotelPe_Backend.listener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelpe.HotelPe_Backend.dto.BookingDTO;
import com.hotelpe.HotelPe_Backend.entity.Booking;
import com.hotelpe.HotelPe_Backend.repo.BookingRepository;
import com.hotelpe.HotelPe_Backend.utils.Utils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentListener {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ObjectMapper mapper;

    @SqsListener( value = "${spring.cloud.aws.sqs.endpoint.booking}")
    public void saveBooking(Message<?> message) {
        try {
            String jsonPayload = (String) message.getPayload();
            log.info("saveBooking.init " + jsonPayload);
            if (!mapper.readTree(jsonPayload).isObject()) {
                log.error("Invalid JSON payload received: {}", jsonPayload);
                return;
            }
            BookingDTO bookingDTO = mapper.readValue(jsonPayload,  BookingDTO.class);
            log.info("saveBooking.saveBooking " + bookingDTO.getId());
            log.info("saveBooking.saveBooking " + bookingDTO.getCheckInDate());
            log.info("saveBooking.saveBooking " + bookingDTO.getCheckOutDate());
            log.info("saveBooking.saveBooking " + bookingDTO.getBookingConfirmationCode());

            Booking booking = Utils.mapBookingDTOEntityToBooking(bookingDTO);
            log.info("saveBooking.saveBooking " + booking.getId());
            bookingRepository.save(booking);
            Acknowledgement.acknowledge(message);

        }catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }
    }
}
