package com.wetark.main.controller;

import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventService;
import com.wetark.main.model.matchedTrade.MatchedTradeService;
import com.wetark.main.model.trade.TradeService;
import com.wetark.main.model.user.User;
import com.wetark.main.security.services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/event")
public class EventController{
    private final EventService eventService;
    private final MatchedTradeService matchedTradeService;
    private final TradeService tradeService;

    public EventController(EventService eventService, MatchedTradeService matchedTradeService, TradeService tradeService) {
        this.eventService = eventService;
        this.matchedTradeService = matchedTradeService;
        this.tradeService = tradeService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public Event add(@RequestBody Event entity) {
        return eventService.add(entity);
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public boolean deleteById(@PathVariable String id){
        return eventService.deleteById(id) != null;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Map<String, Object> findById(String id, String page, String size) {
        Map<String, Object> response = new HashMap<>();
        Event event = eventService.findById(id);
        User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        response.put("event", event);
        response.put("userPendingTrades", tradeService.userPendingOrderOfEvent(user, event, page, size));
        response.put("userMatchedTrades", matchedTradeService.allMatchedTradeForEventOfUser(user, event, page, size));
        response.put("matchedTrades", matchedTradeService.allMatchedTradeForEvent(event, page, size));
        return response;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public List<Event> findAll(String page, String size){
        return eventService.findAll(page, size);
    }
}
