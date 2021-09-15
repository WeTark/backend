package com.wetark.main.payload.request;

import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.Trade;
import com.wetark.main.model.trade.TradeType;
import com.wetark.main.model.user.User;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class TradeRequest {
    @NotBlank
    private TradeType tradeType;

    @NotBlank
    private BigDecimal price;

    @NotBlank
    private BigDecimal size;

//    @NotBlank
//    private String user_id;

    @NotBlank
    private String event_id;

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public Trade newTrade(User user, Event event) {
        Trade trade = new Trade();
        trade.setTradeType(this.tradeType);
        trade.setPrice(this.price);
        trade.setSize(this.size);
        trade.setUser(user);
        trade.setEvent(event);
        return trade;
    }
}
