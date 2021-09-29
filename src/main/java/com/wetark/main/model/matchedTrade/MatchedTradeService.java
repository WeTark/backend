package com.wetark.main.model.matchedTrade;

import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventService;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.response.MatchedTradeSumResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MatchedTradeService {
    private final MatchedTradeRepository matchedTradeRepository;
    private final EventService eventService;

    public MatchedTradeService(MatchedTradeRepository matchedTradeRepository, EventService eventService) {
        this.matchedTradeRepository = matchedTradeRepository;
        this.eventService = eventService;
    }
    public List<MatchedTrade> allMatchedTradeForEvent(Event event, String page, String size){
        return matchedTradeRepository.findAllByEventOrderByCreatedAtDesc(event, PageHelper.pageable(page,size));
    }
    public List<MatchedTrade> allMatchedTradeForEventOfUser(User user, Event event, String page, String size){
        return matchedTradeRepository.findAllTradeByUserAndByEventOrderByCreatedAtDesc(user, event);
    }

    public Map<String, BigDecimal> findGraphData(String eventId) {
        final ConcurrentMap<String, BigDecimal> countMap =
                new ConcurrentHashMap<String, BigDecimal>();
        Map<String, BigDecimal> graphData = new HashMap<>();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Event event = eventService.findById(eventId);

        Instant startDate = Instant.now();
        Instant before = startDate.minus(Duration.ofDays(8));
        Instant dateAfter = before.isAfter(event.createdAt.toInstant())? before: event.createdAt.toInstant();

        List<MatchedTrade> matchedTradeList = matchedTradeRepository.findAllByEventWithCreatedAtBefore(eventService.findById(eventId), Date.from(dateAfter));
        for(MatchedTrade matchedTrade: matchedTradeList){
            String createdAt = formatter.format(matchedTrade.createdAt);
            countMap.putIfAbsent(createdAt,BigDecimal.ZERO);
            countMap.put(createdAt, countMap.get(createdAt).add(BigDecimal.ONE));
            graphData.putIfAbsent(createdAt, BigDecimal.ZERO);
            graphData.put(createdAt, graphData.get(createdAt).add(matchedTrade.getYesTrade().getPrice()));
        }
        BigDecimal yesValue = BigDecimal.valueOf(5);
        for (Instant date = dateAfter; !date.truncatedTo(ChronoUnit.DAYS).isAfter(startDate.truncatedTo(ChronoUnit.DAYS)); date = date.plus(Duration.ofDays(1)))
        {
            String createdAt = formatter.format(Date.from(date));
            graphData.putIfAbsent(createdAt, yesValue);
            countMap.putIfAbsent(createdAt,BigDecimal.ONE);
            graphData.put(createdAt, graphData.get(createdAt).divide(countMap.get(createdAt), 2, RoundingMode.HALF_UP));
            yesValue =  graphData.get(createdAt);
        }
        return new TreeMap<String, BigDecimal>(graphData);
    }

    public MatchedTradeSumResponse findSumOfTrade(String eventId){
        return matchedTradeRepository.findSumOfAllTradeByEvent(eventService.findById(eventId));
    }

}
