package com.wetark.main.controller;

import com.wetark.main.exception.CustomException;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventService;
import com.wetark.main.model.event.tag.Tag;
import com.wetark.main.model.matchedTrade.MatchedTradeService;
import com.wetark.main.model.trade.TradeService;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.request.CreatePersonalEventRequest;
import com.wetark.main.payload.request.EventRequest;
import com.wetark.main.security.services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Event add(@RequestBody EventRequest entity) {
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
    @GetMapping("/tag/all")
    public List<Tag> findAllTag(String page, String size) throws CustomException {
        return eventService.findAllTag(page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public List<Event> findAll(String page, String size, @RequestParam(required = false) String tag) throws CustomException {
        return eventService.findAllNonPrivate(page, size, tag);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all/personal")
    public List<Event> findAllPersonal(String page, String size){
        User user =  ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return eventService.findAllByUser(user, page, size);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/event-variable")
    public CreatePersonalEventRequest findEventVariable(String eventId) throws CustomException {
        return eventService.eventVariables(eventId);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-personal-event")
    public String addPersonalEvent(@RequestBody CreatePersonalEventRequest createPersonalEventRequest) throws CustomException {
        User user =  ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return eventService.createPersonalEvent(createPersonalEventRequest, user);
    }

}
