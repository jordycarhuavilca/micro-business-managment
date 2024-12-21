package com.hotelpe.HotelPe_Backend.service.impl;

import com.hotelpe.HotelPe_Backend.dto.YapePaymentDto;
import com.hotelpe.HotelPe_Backend.dto.YapeResponseDto;
import com.hotelpe.HotelPe_Backend.interfaces.ICardPayment;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YapePaymentService  implements ICardPayment {
    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;


    @Override
    public <T, R> R processPayment(T paymentRequest) {

        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            if (!(paymentRequest instanceof YapePaymentDto)) {
                throw new IllegalArgumentException("Unsupported payment request type");
            }
            YapePaymentDto yapePaymentDto = (YapePaymentDto) paymentRequest;

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest createRequest =
                    PaymentCreateRequest.builder()
                            .description(yapePaymentDto.getDescription())
                            .installments(1)
                            .payer(PaymentPayerRequest.builder()
                                    .email(yapePaymentDto.getPayer().getEmail())
                                    .build())
                            .paymentMethodId(yapePaymentDto.getPaymentMethodId())
                            .token(yapePaymentDto.getToken())
                            .transactionAmount(yapePaymentDto.getTransactionAmount())
                            .build();

            Payment payment = client.create(createRequest);
            YapeResponseDto responseDTO =  new YapeResponseDto(payment.getId(),
                    String.valueOf(payment.getStatus()),
                    payment.getStatusDetail());

            log.info("YapePaymentService.processPayment " + responseDTO);
            return (R) responseDTO;
        } catch (MPException e) {
            System.out.println("MPException " + e.getMessage());
            throw new RuntimeException(e);
        } catch (MPApiException e) {
            var apiResponse = e.getApiResponse();
            var content = apiResponse.getContent();
            System.out.println("error  content -... "  + content);
            throw new RuntimeException(e);
        }
    }
}
