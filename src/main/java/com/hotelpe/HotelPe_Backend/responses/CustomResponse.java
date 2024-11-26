package com.hotelpe.HotelPe_Backend.responses;

import jakarta.annotation.Nullable;
import org.apache.http.HttpStatus;

public record CustomResponse<T>(HttpStatus http, @Nullable T body, @Nullable String error) {
}
