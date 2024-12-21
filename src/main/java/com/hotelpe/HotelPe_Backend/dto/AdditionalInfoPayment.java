package com.hotelpe.HotelPe_Backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalInfoPayment {
    @JsonProperty(namespace = "item")
    private BookingDTO item;
}
