package com.wetark.main.model.trade;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.wetark.main.constant.Errors;
import com.wetark.main.exception.CustomException;
import com.wetark.main.model.BaseService;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventRepository;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.UserRepository;
import com.wetark.main.payload.request.TradeRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

    public Trade addTrade(TradeRequest tradeRequest) throws CustomException {
        Event event = eventRepository.findById(tradeRequest.getEvent_id()).orElseThrow(()->new CustomException(Errors.EVENT_NOT_FOUND, "400"));
        User user = userRepository.findById(tradeRequest.getUser_id()).orElseThrow(()->new CustomException(Errors.USER_NOT_FOUND, "400"));
        Trade trade = tradeRequest.newTrade(user, event);
        processOrder(trade);
        return this.add(trade);
    }

    public void processOrder(Trade reqTrade) throws CustomException {
        Multimap<BigDecimal, Trade> mapOfTrades = ArrayListMultimap.create();
        TradeType action = reqTrade.getTradeType();
//        validateOrderRequest(req);

        switch (action) {
            case YES:
                List<Trade> noTrades = tradeRepository.getAllSellOrdersSortedByPriceAndActive(TradeType.NO, reqTrade.getEvent());
                mapOfTrades = noTrades.isEmpty() ? ArrayListMultimap.create() : getOrderMap(noTrades, TradeType.NO);
                matchOrders(reqTrade, mapOfTrades, TradeType.YES);

//                return buyOrderResponse;

            case NO:
                List<Trade> yesTrades = tradeRepository.getAllBuyOrdersSortedByPriceAndActive(TradeType.YES, reqTrade.getEvent());
                mapOfTrades = yesTrades.isEmpty() ? ArrayListMultimap.create() :  getOrderMap(yesTrades, TradeType.YES);
                matchOrders(reqTrade, mapOfTrades, TradeType.NO);
//                return sellOrderResponse;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }
    }

    private void matchOrders(Trade reqTrade, Multimap<BigDecimal,Trade> mapOfTrades, TradeType tradeType){
        List<BigDecimal> listOfSelectedOrders = reArrangeOrderByprice(reqTrade, mapOfTrades);
        if (listOfSelectedOrders.isEmpty()) {

        }else{
            for (BigDecimal price : listOfSelectedOrders) {
                System.out.println(price);
            }
        }
        System.out.println(listOfSelectedOrders);
    }
    private List<BigDecimal> reArrangeOrderByprice(Trade reqTrade, Multimap<BigDecimal, Trade> mapOfTrades) {
        System.out.println(mapOfTrades);
        return mapOfTrades.keys().stream()
                .filter(key -> key.add(reqTrade.getPrice()).compareTo(BigDecimal.TEN)>=0)
                .sorted()
                .collect(Collectors.toList());
    }
    private Multimap<BigDecimal, Trade> getOrderMap(List<Trade> sellOrders, TradeType tradeType) throws CustomException {
        Multimap<BigDecimal, Trade> mapOfOrders = ArrayListMultimap.create();
        switch (tradeType) {
            case YES:
                List<Trade> sortedYesTrades = sellOrders.stream().sorted(Comparator.comparing(or -> or.getCreatedAt())).collect(Collectors.toList());
                sortedYesTrades.stream()
                        .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                        .forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            case NO:
                List<Trade> sortedNoTrades = sellOrders.stream().sorted(Comparator.comparing(or -> or.getCreatedAt())).collect(Collectors.toList());
                sortedNoTrades.stream()
                        .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                        .forEach(or -> mapOfOrders.put(or.getPrice(), or));
                return mapOfOrders;
            default:
                throw new CustomException(Errors.INVALID_SIDE, "400");
        }

    }

}
