package com.wetark.main.payload.response;

import java.math.BigDecimal;

public interface MatchedTradeSumResponse {
    public BigDecimal getYesSum();
    public BigDecimal getNoSum();
}
