package com.hotelpe.HotelPe_Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class YapeResponseDto {
    private Long id;
    private String status;
    private String detail;
}
