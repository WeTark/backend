package com.wetark.main.payload.response.userPortfolio;

import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.TradeType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserPortfolioResponse {
    private String id;
    private String title;
    private String picture;
    private Map<TradeType, UserPortfolioAmount> amount;
    private Date createdAt;
    private Date expireAt;

    public UserPortfolioResponse(UserPortfolio userPortfolio, Event event) {
        this.id = userPortfolio.getId();
        this.title = event.getTitle();
        this.picture = event.getPicture();
        this.createdAt = event.getCreatedAt();
        this.expireAt = event.getExpireAt();
        this.amount = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<TradeType, UserPortfolioAmount> getAmount() {
        return amount;
    }

    public void setAmount(Map<TradeType, UserPortfolioAmount> amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
