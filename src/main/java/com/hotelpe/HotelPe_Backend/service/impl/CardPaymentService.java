package com.hotelpe.HotelPe_Backend.service.impl;
import com.hotelpe.HotelPe_Backend.config.SqsMessageProducer;
import com.hotelpe.HotelPe_Backend.dto.CardPaymentDTO;
import com.hotelpe.HotelPe_Backend.enumFiles.PaymentSatus;
import com.hotelpe.HotelPe_Backend.exception.MercadoPagoException;
import com.hotelpe.HotelPe_Backend.interfaces.ICardPayment;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.hotelpe.HotelPe_Backend.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CardPaymentService implements ICardPayment {
    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;

    @Override
    public <T, R> R processPayment(T paymentRequest) {
        if (!(paymentRequest instanceof CardPaymentDTO)) {
            throw new IllegalArgumentException("Unsupported payment request type");
        }
        CardPaymentDTO cardPaymentDTO = (CardPaymentDTO) paymentRequest;
        log.info("processPayment.init1 " + cardPaymentDTO);
        log.info("processPayment.init2 " + cardPaymentDTO.getPayer().getEmail());
        log.info("processPayment.init3 " + cardPaymentDTO.getAdditionalInfo());
        log.info("processPayment.init4 " + cardPaymentDTO.getPayer().getIdentification().getNumber());

        try {
            // Configure MercadoPago with access token
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            // Build PaymentCreateRequest
            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(cardPaymentDTO.getTransactionAmount())
                            .token(cardPaymentDTO.getToken())
                            .description(cardPaymentDTO.getProductDescription())
                            .installments(cardPaymentDTO.getInstallments())
                            .paymentMethodId(cardPaymentDTO.getPaymentMethodId())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(cardPaymentDTO.getPayer().getEmail())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(cardPaymentDTO.getPayer().getIdentification().getType())
                                                            .number(cardPaymentDTO.getPayer().getIdentification().getNumber())
                                                            .build())
                                            .build())
                            .build();

            // Send request to MercadoPago
            PaymentClient paymentClient = new PaymentClient();
            Payment createdPayment = paymentClient.create(paymentCreateRequest);

            log.info("processPayment.response.status: " + createdPayment.getStatus());
            log.info("processPayment.response.detail: " + createdPayment.getStatusDetail());

            // Create PaymentResponseDTO and return it
            PaymentResponseDTO responseDTO = new PaymentResponseDTO(
                    createdPayment.getId(),
                    String.valueOf(createdPayment.getStatus()),
                    createdPayment.getStatusDetail());

            return (R) responseDTO;

        } catch (MPApiException apiException) {
            log.error("API Exception: " + apiException.getApiResponse().getContent());
            throw new MercadoPagoException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            log.error("Exception: " + exception.getMessage());
            throw new MercadoPagoException(exception.getMessage());
        }
    }
}
