package com.hotelpe.HotelPe_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HotelPeBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(HotelPeBackendApplication.class, args);
	}
}