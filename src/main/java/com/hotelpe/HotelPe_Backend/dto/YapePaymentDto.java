package com.hotelpe.HotelPe_Backend.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class YapePaymentDto {
    private String token;
    private BigDecimal transactionAmount;
    private int installments;
    private String description;
    private String paymentMethodId;
    private PayerYapeDto payer;
}
