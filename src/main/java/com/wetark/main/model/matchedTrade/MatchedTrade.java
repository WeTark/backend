package com.wetark.main.model.matchedTrade;

import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.Trade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(	name = "matched_trade")
public class MatchedTrade extends Base {

    @ManyToOne()
    @JoinColumn(name="event_id")
    private Event event;

    @OneToOne
    @NotNull
    private Trade yesTrade;

    @OneToOne
    @NotNull
    private Trade noTrade;

    public MatchedTrade() {
    }

    public MatchedTrade(Event event, Trade yesTrade, Trade noTrade, BigDecimal size) {
        this.event = event;
        this.yesTrade = yesTrade;
        this.noTrade = noTrade;
        this.size = size;
    }

    private BigDecimal size = BigDecimal.ONE;

    public Trade getYesTrade() {
        return yesTrade;
    }

    public void setYesTrade(Trade yesTrade) {
        this.yesTrade = yesTrade;
    }

    public Trade getNoTrade() {
        return noTrade;
    }

    public void setNoTrade(Trade noTrade) {
        this.noTrade = noTrade;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }
}
