package com.wetark.main.payload.response;

import java.math.BigDecimal;

public interface PendingTradeResponse {

    public BigDecimal getPrice();
    public BigDecimal getSize();

}
