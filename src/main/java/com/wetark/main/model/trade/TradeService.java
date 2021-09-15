package com.wetark.main.model.trade;

import com.wetark.main.constant.Errors;
import com.wetark.main.exception.CustomException;
import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventRepository;
import com.wetark.main.model.matchedTrade.MatchedTrade;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.UserRepository;
import com.wetark.main.payload.request.TradeRequest;
import com.wetark.main.payload.response.PendingTradeResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TradeService extends BaseService<Trade> {
    private final TradeRepository tradeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TradeService(TradeRepository tradeRepository, EventRepository eventRepository, UserRepository userRepository) {
        super(tradeRepository);
        this.tradeRepository = tradeRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Map<String,  List<PendingTradeResponse>> topPendingTrade(String eventId, String page, String size) throws CustomException {
        Event event = eventRepository.findById(eventId).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        Map<String, List<PendingTradeResponse>> responseData = new HashMap<>();
        responseData.put("YES", tradeRepository.getTopPendingTrade(TradeType.YES, event, PageHelper.pageable(page, size)));
        responseData.put("NO", tradeRepository.getTopPendingTrade(TradeType.NO, event, PageHelper.pageable(page, size)));
        return responseData;
    }

    public List<Trade> userPendingOrderOfEvent(User user, Event event, String page, String size){
//        Event event = eventRepository.findById(eventId).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        return tradeRepository.findByUserAndEventAndIsActiveOrderByCreatedAtDesc(user, event,true,PageHelper.pageable(page, size));
    }

    public Trade addTrade(TradeRequest tradeRequest, User user) throws CustomException {
        Event event = eventRepository.findById(tradeRequest.getEvent_id()).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        Trade trade = tradeRequest.newTrade(user, event);
        return processTrade(trade);
    }

    public Trade processTrade(Trade reqTrade) throws CustomException {
        TradeType action = reqTrade.getTradeType();
//        validateOrderRequest(req);
        switch (action) {
            case YES:
                List<Trade> noTrades = tradeRepository.getAllTradesSortedByPriceAndActive(TradeType.NO, reqTrade.getEvent());
                matchOrders(reqTrade, noTrades, reqTrade.getEvent());
                return reqTrade;

            case NO:
                List<Trade> yesTrades = tradeRepository.getAllTradesSortedByPriceAndActive(TradeType.YES, reqTrade.getEvent());
                matchOrders(reqTrade, yesTrades, reqTrade.getEvent());
                return reqTrade;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }
    }

    private void matchOrders(Trade reqTrade, List<Trade> toMatchTrades, Event event){
        List<Trade> tradesToSave = new ArrayList<>();
        tradesToSave.add(reqTrade);
        if (toMatchTrades.isEmpty()) {
            tradeRepository.save(reqTrade);
        }else{
            int i = 0;
            while (reqTrade.getSize().compareTo(BigDecimal.ZERO)>0 && i<toMatchTrades.size() && toMatchTrades.get(i).getPrice().add(reqTrade.getPrice()).compareTo(BigDecimal.TEN)>=0){
                Trade matchingTrade = toMatchTrades.get(i);
                BigDecimal matchSize = reqTrade.getSize().compareTo(matchingTrade.getSize()) > 0 ? matchingTrade.getSize() : reqTrade.getSize();
                if(reqTrade.getSize().compareTo(matchingTrade.getSize()) > 0){
                    reqTrade.setSize(reqTrade.getSize().subtract(matchingTrade.getSize()));
                    matchingTrade.setSize(BigDecimal.ZERO);
                    matchingTrade.setActive(false);
                }else if(reqTrade.getSize().compareTo(matchingTrade.getSize()) == 0){
                    reqTrade.setSize(BigDecimal.ZERO);
                    reqTrade.setActive(false);
                    matchingTrade.setSize(BigDecimal.ZERO);
                    matchingTrade.setActive(false);
                }else{
                    matchingTrade.setSize(matchingTrade.getSize().subtract(reqTrade.getSize()));
                    reqTrade.setSize(BigDecimal.ZERO);
                    reqTrade.setActive(false);
                }
                calculatePrice(event, reqTrade, matchingTrade);

                if(reqTrade.getTradeType() == TradeType.YES) {
                    event.getMatchedTrade().add(new MatchedTrade(event, reqTrade, matchingTrade, matchSize));
                }else{
                    event.getMatchedTrade().add(new MatchedTrade(event, matchingTrade, reqTrade, matchSize));
                }
                tradesToSave.add(matchingTrade);
                i++;
            }
            tradeRepository.saveAll(tradesToSave);
        }
    }

    private void calculatePrice(Event event, Trade reqTrade, Trade matchingTrade) {
        switch (reqTrade.getTradeType()){
            case YES:
                price(event, matchingTrade, reqTrade);
                break;
            case NO:
                price(event, reqTrade, matchingTrade);
                break;
        }
    }

    private void price(Event event, Trade noTrade, Trade yesTrade) {
        if(yesTrade.getPrice().compareTo(event.getYesPrice())>0){
            if(noTrade.getPrice().compareTo(event.getNoPrice())>0){
                yesTrade.setPrice(event.getYesPrice());
                noTrade.setPrice(event.getNoPrice());
            } else {
                yesTrade.setPrice(BigDecimal.TEN.subtract(noTrade.getPrice()));
                event.setYesPrice(BigDecimal.TEN.subtract(noTrade.getPrice()));
                event.setNoPrice(noTrade.getPrice());
            }
        }else {
            noTrade.setPrice(BigDecimal.TEN.subtract(yesTrade.getPrice()));
            event.setYesPrice(yesTrade.getPrice());
            event.setNoPrice(BigDecimal.TEN.subtract(yesTrade.getPrice()));
        }
    }
}
