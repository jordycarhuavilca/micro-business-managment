package com.hotelpe.HotelPe_Backend.interfaces;


public interface IPaymentProcess {
     <T, R> R processPayment(T paymentRequest);
}
