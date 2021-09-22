package com.wetark.main.controller;

import com.wetark.main.exception.CustomException;
import com.wetark.main.kafka.KafKaProducerService;
import com.wetark.main.model.trade.Trade;
import com.wetark.main.model.trade.TradeService;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.request.TradeRequest;
import com.wetark.main.payload.response.PendingTradeResponse;
import com.wetark.main.security.services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return tradeService.addTrade(tradeRequest, userDetails.getUser());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('USER')")
    public Map<String,  List<PendingTradeResponse>> fetchTopPendingTrade(@RequestParam String eventId, @RequestParam String page,@RequestParam String size) throws CustomException {
        return tradeService.topPendingTrade(eventId, page, size);
    }


//    @GetMapping("/pending/user")
//    @PreAuthorize("hasRole('USER')")
//    public List<Trade> fetchUserPendingPendingTradeForEvent(@RequestParam String eventId, @RequestParam String page,@RequestParam String size) throws CustomException {
//        User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        return tradeService.userPendingOrderOfEvent(user, eventId, page, size);
//    }
}
