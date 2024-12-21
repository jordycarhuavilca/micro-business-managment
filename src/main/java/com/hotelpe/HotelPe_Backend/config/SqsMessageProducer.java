package com.hotelpe.HotelPe_Backend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SqsMessageProducer {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private SqsTemplate sqsTemplate;


    public <T> void send(T message,String queueName ,Map<String, Object> headers) throws JsonProcessingException {
        if (message == null) {
            log.warn("SQS Producer cant produce 'null' value");
            return;
        }


        String value  = mapper.writeValueAsString((Object) message);
        sqsTemplate.send(sqsSendOptions ->
                sqsSendOptions
                        .queue(queueName)
                        .headers(headers)
                        .payload(value)
        );
    }
}
