package com.wetark.main.payload.response.userPortfolio;

import java.math.BigDecimal;

public class UserPortfolioAmount {
    private  BigDecimal totalAmount;
    private  BigDecimal totalPendingAmount;

    public UserPortfolioAmount(BigDecimal totalAmount, BigDecimal totalPendingAmount) {
        this.totalAmount = totalAmount;
        this.totalPendingAmount = totalPendingAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalPendingAmount() {
        return totalPendingAmount;
    }

    public void setTotalPendingAmount(BigDecimal totalPendingAmount) {
        this.totalPendingAmount = totalPendingAmount;
    }
}
