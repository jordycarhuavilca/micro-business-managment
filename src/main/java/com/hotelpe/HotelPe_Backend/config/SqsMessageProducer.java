package com.hotelpe.HotelPe_Backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SqsMessageProducer {
    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${auth.queue.name}")
    private String ordersQ;

    @Autowired
    public SqsMessageProducer(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    public <T> void send(T message, Map<String, Object> headers) {
        if (message == null) {
            log.warn("SQS Producer cant produce 'null' value");
            return;
        }

        log.debug(" Messgae {} " + message);
        log.debug(" Queue name {} " + ordersQ);
        queueMessagingTemplate.convertAndSend(ordersQ, message, headers);
    }
}
