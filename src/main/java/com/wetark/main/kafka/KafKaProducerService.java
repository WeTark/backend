package com.wetark.main.kafka;

import com.sun.deploy.association.utility.AppConstants;
import com.wetark.main.model.trade.Trade;
import com.wetark.main.payload.request.TradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafKaProducerService
{
    private static final Logger logger =
            LoggerFactory.getLogger(KafKaProducerService.class);

    @Autowired
    private KafkaTemplate<String, TradeRequest> kafkaTemplate;

    public void sendMessage(String key, TradeRequest trade)
    {
        logger.info(String.format("Message sent -> %s", trade.getEvent_id(), "->", trade.getTradeType()));
        this.kafkaTemplate.send("first_topic_3", key, trade);
    }
}