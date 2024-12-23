package com.hotelpe.HotelPe_Backend.utils;

import com.hotelpe.HotelPe_Backend.dto.BookingDTO;
import com.hotelpe.HotelPe_Backend.dto.RoomDTO;
import com.hotelpe.HotelPe_Backend.dto.UserDTO;
import com.hotelpe.HotelPe_Backend.entity.Booking;
import com.hotelpe.HotelPe_Backend.entity.Room;
import com.hotelpe.HotelPe_Backend.entity.User;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Random RANDOM = new SecureRandom();
    private static final int ID_LENGTH = 4;

    public static Integer generateNumericId() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
            int digit = RANDOM.nextInt(10);
            sb.append(digit);
        }
        return Integer.parseInt(sb.toString());
    }

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }


    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static User mapUserDTOToUserEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());
        return user;
    }

    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());
        return roomDTO;
    }

    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        // Map simple fields
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate().toString());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate().toString());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList()));
        }
        return roomDTO;
    }
    public static Booking mapBookingDTOEntityToBooking(BookingDTO bookingDTO){
        Booking booking = new Booking();
        // Map simple fields
        booking.setId(bookingDTO.getId());
        booking.setCheckInDate(LocalDate.parse(bookingDTO.getCheckInDate()));
        booking.setCheckOutDate(LocalDate.parse(bookingDTO.getCheckOutDate()));
        booking.setNumOfAdults(bookingDTO.getNumOfAdults());
        booking.setNumOfChildren(bookingDTO.getNumOfChildren());
        booking.setTotalNumOfGuest(bookingDTO.getTotalNumOfGuest());
        booking.setBookingConfirmationCode(bookingDTO.getBookingConfirmationCode());

        if (bookingDTO.getUser() != null) {
            User user = new User();
            UserDTO userDTO = bookingDTO.getUser();
            user.setId(userDTO.getId());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setName(userDTO.getName());

            booking.setUser(user);
        }
        if (bookingDTO.getRoom() != null) {
            Room room = new Room();

            room.setId(bookingDTO.getRoom().getId());
            room.setRoomType(bookingDTO.getRoom().getRoomType());
            room.setRoomPrice(bookingDTO.getRoom().getRoomPrice());
            room.setRoomPhotoUrl(bookingDTO.getRoom().getRoomPhotoUrl());
            room.setRoomDescription(bookingDTO.getRoom().getRoomDescription());
            booking.setRoom(room);
        }
        return booking;
    }
    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {

        BookingDTO bookingDTO = new BookingDTO();
        // Map simple fields
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate().toString());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate().toString());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        if (mapUser) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser()));
        }
        if (booking.getRoom() != null) {
            RoomDTO roomDTO = new RoomDTO();

            roomDTO.setId(booking.getRoom().getId());
            roomDTO.setRoomType(booking.getRoom().getRoomType());
            roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
            bookingDTO.setRoom(roomDTO);
        }
        return bookingDTO;
    }

    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream().map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false)).collect(Collectors.toList()));
        }
        return userDTO;
    }


    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream().map(Utils::mapRoomEntityToRoomDTO).collect(Collectors.toList());
    }

    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }

    public static User mapUserDtoToUserEntity(UserDTO user){
        User usuario = new User();
        usuario.setEmail(user.getEmail());
        usuario.setPassword(user.getPassword());
        usuario.setName(user.getName());
        usuario.setPhoneNumber(user.getPhoneNumber());
        usuario.setRole(user.getRole());

        return usuario;
    }

}
