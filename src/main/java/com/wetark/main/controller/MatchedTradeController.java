package com.wetark.main.controller;

import com.wetark.main.model.matchedTrade.MatchedTradeService;
import com.wetark.main.payload.response.MatchedTradeSumResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/matched-trade")
public class MatchedTradeController {
    private final MatchedTradeService matchedTradeService;


    public MatchedTradeController(MatchedTradeService matchedTradeService) {
        this.matchedTradeService = matchedTradeService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/graph")
    public Map<String, BigDecimal> graphData(@RequestParam String eventId){
        return matchedTradeService.findGraphData(eventId);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/sum")
    public MatchedTradeSumResponse sumData(@RequestParam String eventId){
        return matchedTradeService.findSumOfTrade(eventId);
    }
}
