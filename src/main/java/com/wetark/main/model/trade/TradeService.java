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
import com.wetark.main.model.user.notification.Notification;
import com.wetark.main.model.user.notification.NotificationType;
import com.wetark.main.payload.request.TradeRequest;
import com.wetark.main.payload.response.PendingTradeResponse;
import com.wetark.main.payload.response.userPortfolio.UserPortfolio;
import com.wetark.main.payload.response.userPortfolio.UserPortfolioAmount;
import com.wetark.main.payload.response.userPortfolio.UserPortfolioResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<UserPortfolioResponse> userPortfolio(User user){
        List<UserPortfolio> userPortfolioList = tradeRepository.findUserPortfolioByUser(user);
        Map<String, UserPortfolioResponse> userPortfolioResponseMap= new HashMap<>();
        userPortfolioList.forEach(userPortfolio -> {
            userPortfolioResponseMap.putIfAbsent(userPortfolio.getId(), new UserPortfolioResponse(userPortfolio, eventRepository.findById(userPortfolio.getId()).get()));
            userPortfolioResponseMap.get(
                    userPortfolio.getId()).getAmount().put(userPortfolio.getTradeType(),
                    new UserPortfolioAmount(userPortfolio.getTotalAmount(), userPortfolio.getTotalPendingAmount()));
        });
        return new ArrayList<>(userPortfolioResponseMap.values());
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
        if(tradeRequest.getInitialSize().multiply(tradeRequest.getPrice()).compareTo(user.getBalance().getAvailableBalance()) == 1){
            user.getNotifications().add(new Notification(NotificationType.INSUFFICIENT_BALANCE, "Insufficient balance to invest for ₹" + tradeRequest.getInitialSize().multiply(tradeRequest.getPrice()),tradeRequest.getEvent_id()));
            userRepository.save(user);
            throw new CustomException(Errors.INSUFFICIENT_BALANCE, "400");
        }
        BigDecimal amountFlow = tradeRequest.getInitialSize().multiply(tradeRequest.getPrice());
        user.getBalance().setAvailableBalance(user.getBalance().getAvailableBalance().subtract(amountFlow));
        user.getBalance().setOnHoldBalance(user.getBalance().getOnHoldBalance().add(amountFlow));
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
                MatchedTrade matchedTrade = new MatchedTrade(event, reqTrade, matchingTrade, matchSize);
                event.getMatchedTrade().add(matchedTrade);
                calculatePrice(event, matchedTrade);

                matchedTrade.newMatch();

                tradesToSave.add(matchingTrade);
                i++;
            }
            tradeRepository.saveAll(tradesToSave);
            userRepository.saveAll(tradesToSave.stream().map(trade -> trade.getUser()).collect(Collectors.toList()));
        }
        if(reqTrade.getActive() == true){
            reqTrade.getUser().getNotifications().add(
                    new Notification(
                            NotificationType.TRADE_IN_PROCESS,
                            "₹"+reqTrade.getPrice() +" * " + reqTrade.getSize() + " Qty " + reqTrade.getTradeType() + " trade in process for event: " + event.getTitle(),
                            event.getId())
            );
        }
        userRepository.save(reqTrade.getUser());
    }

    private void calculatePrice(Event event, MatchedTrade matchedTrade) {
        BigDecimal yesPrice = BigDecimal.ZERO;
        BigDecimal noPrice = BigDecimal.ZERO;

        if(matchedTrade.getYesTrade().getPrice().compareTo(event.getYesPrice())>0){
            if(matchedTrade.getNoTrade().getPrice().compareTo(event.getNoPrice())>0){
                yesPrice = event.getYesPrice();
                noPrice = event.getNoPrice();
            } else {
                yesPrice = BigDecimal.TEN.subtract(matchedTrade.getNoTrade().getPrice());
                noPrice = matchedTrade.getNoTrade().getPrice();
            }
        }else {
            yesPrice = matchedTrade.getYesTrade().getPrice();
            noPrice = BigDecimal.TEN.subtract(matchedTrade.getYesTrade().getPrice());
        }

        matchedTrade.setYesPrice(yesPrice);
        matchedTrade.setNoPrice(noPrice);
        event.setYesPrice(yesPrice);
        event.setNoPrice(noPrice);
    }
}
