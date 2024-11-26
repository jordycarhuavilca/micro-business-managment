package com.hotelpe.HotelPe_Backend.service.interfac;

import com.hotelpe.HotelPe_Backend.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response addNewRoom(MultipartFile photo, String roomType, Double roomPrice, String description);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(int roomId);

    Response updateRoom(int roomId, String description, String roomType, Double roomPrice, MultipartFile photo);

    Response getRoomById(int roomId);

    Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    Response getAllAvailableRooms();
}
