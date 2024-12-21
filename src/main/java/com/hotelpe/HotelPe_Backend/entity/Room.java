package com.hotelpe.HotelPe_Backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomType;
    private Double roomPrice;
    private String roomPhotoUrl;
    private String roomDescription;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
     @Override
        public String toString() {
            return "Room{" +
                    "id=" + id +
                    ", roomType='" + roomType + '\'' +
                    ", roomPrice=" + roomPrice +
                    ", roomPhotoUrl='" + roomPhotoUrl + '\'' +
                    ", roomDescription='" + roomDescription + '\'' +
                    '}';
        }
}
