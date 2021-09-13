package com.wetark.main.model.matchedTrade;

import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.Trade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(	name = "matched_trade")
public class MatchedTrade  extends Base {

    @OneToOne
    @NotBlank
    private Event event;

    @OneToOne
    @NotBlank
    private Trade yesTrade;

    @OneToOne
    @NotBlank
    private Trade noTrade;

    private BigDecimal size = BigDecimal.ONE;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

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
