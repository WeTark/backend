package com.wetark.main.kafka;

import com.sun.deploy.association.utility.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class KafKaConsumerService
{
    private final Logger logger =
            LoggerFactory.getLogger(KafKaConsumerService.class);

    @KafkaListener(topics = "first_topic",
            groupId = "1")
    public void consume(String message)
    {
        logger.info(String.format("Message recieved -> %s", message));
    }
}