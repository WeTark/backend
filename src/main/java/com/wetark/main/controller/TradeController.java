package com.wetark.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetark.main.exception.CustomException;
import com.wetark.main.kafka.KafKaProducerService;
import com.wetark.main.model.trade.Trade;
import com.wetark.main.model.trade.TradeService;
import com.wetark.main.payload.request.TradeRequest;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController{
    private final TradeService tradeService;
    private final KafKaProducerService kafKaProducerService;

    public TradeController(TradeService tradeService, KafKaProducerService kafKaProducerService) {
        this.tradeService = tradeService;
        this.kafKaProducerService = kafKaProducerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public Trade newTrade(@RequestBody TradeRequest tradeRequest) throws CustomException {
        return tradeService.addTrade(tradeRequest);
    }

    //    @PostMapping
//    @PreAuthorize("hasRole('USER')")
//    public Boolean add(@RequestBody TradeRequest tradeRequest) {
//
//        kafKaProducerService.sendMessage(tradeRequest.getEvent_id() ,tradeRequest);
//        return true;
//    }
}
