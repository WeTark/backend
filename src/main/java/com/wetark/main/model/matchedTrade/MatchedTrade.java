package com.wetark.main.model.matchedTrade;

import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.Trade;
import com.wetark.main.model.trade.TradeType;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.notification.Notification;
import com.wetark.main.model.user.notification.NotificationType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(	name = "matched_trade")
public class MatchedTrade extends Base {

    @ManyToOne()
    @JoinColumn(name="event_id")
    private Event event;

    private BigDecimal yesPrice;
    private BigDecimal noPrice;
    @OneToOne
    @NotNull
    private Trade yesTrade;

    @OneToOne
    @NotNull
    private Trade noTrade;

    private BigDecimal size = BigDecimal.ONE;

    public MatchedTrade() {
    }

    public MatchedTrade(Event event, Trade firstTrade, Trade secondTrade, BigDecimal size) {
        this.event = event;
        this.yesTrade = firstTrade.getTradeType() == TradeType.YES ? firstTrade : secondTrade;
        this.noTrade = firstTrade.getTradeType() == TradeType.YES ? secondTrade : firstTrade;;
        this.size = size;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public BigDecimal getYesPrice() {
        return yesPrice;
    }

    public void setYesPrice(BigDecimal yesPrice) {
        this.yesPrice = yesPrice;
    }

    public BigDecimal getNoPrice() {
        return noPrice;
    }

    public void setNoPrice(BigDecimal noPrice) {
        this.noPrice = noPrice;
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

    public void newMatch() {
        User yesUser = this.yesTrade.getUser();
        BigDecimal yesAmount = this.yesPrice.multiply(this.size);
        yesUser.getNotifications().add(
                new Notification(
                        NotificationType.TRADE_MATCHED,
                        "₹"+this.yesPrice +" * " + this.size + " Qty" + " yes trade matched for event: " + event.getTitle(),
                        event.getId())
            );
        yesUser.getBalance().setOnHoldBalance(yesUser.getBalance().getOnHoldBalance().subtract(yesAmount));
        yesUser.getBalance().setInvestedBalance(yesUser.getBalance().getInvestedBalance().add(yesAmount));

        if(this.yesPrice.compareTo(this.yesTrade.getPrice()) != 0){
            BigDecimal diffYesAmount = (this.yesTrade.getPrice().subtract(this.yesPrice)).multiply(this.size);
            yesUser.getBalance().setOnHoldBalance(yesUser.getBalance().getOnHoldBalance().subtract(diffYesAmount));
            yesUser.getBalance().setAvailableBalance(yesUser.getBalance().getAvailableBalance().add(diffYesAmount));
        }

        User noUser = this.noTrade.getUser();
        BigDecimal noAmount = this.noPrice.multiply(this.size);
        noUser.getNotifications().add(
                new Notification(
                        NotificationType.TRADE_MATCHED,
                        "₹"+this.noPrice +" * " + this.size + " Qty" + " no trade matched for event: " + event.getTitle(),
                        event.getId())
        );
        noUser.getBalance().setOnHoldBalance(noUser.getBalance().getOnHoldBalance().subtract(noAmount));
        noUser.getBalance().setInvestedBalance(noUser.getBalance().getInvestedBalance().add(noAmount));

        if(this.noPrice.compareTo(this.noTrade.getPrice()) != 0){
            BigDecimal diffNoAmount = (this.noTrade.getPrice().subtract(this.noPrice)).multiply(this.size);
            noUser.getBalance().setOnHoldBalance(noUser.getBalance().getOnHoldBalance().subtract(diffNoAmount));
            noUser.getBalance().setAvailableBalance(noUser.getBalance().getAvailableBalance().add(diffNoAmount));
        }
    }
}
