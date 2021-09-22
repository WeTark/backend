package com.wetark.main.payload.response.userPortfolio;

import com.wetark.main.model.trade.TradeType;

import java.math.BigDecimal;
import java.util.Date;

public interface UserPortfolio {
    public String getId();
    public String getTitle();
    public Date getCreatedAt();
    public Date getExpireAt();

    public TradeType getTradeType();
    public BigDecimal getTotalAmount();
    public BigDecimal getTotalPendingAmount();
}
