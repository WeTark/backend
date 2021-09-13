package com.wetark.main.model.trade;

import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(	name = "trade")
public class Trade extends Base {
    @Id
    @GeneratedValue(generator = generatorName)
    @GenericGenerator(name = generatorName, strategy = "uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TradeType tradeType;

    private BigDecimal price;
    private BigDecimal size;

    @OneToOne
    @NotNull
    private User user;

    @OneToOne
    @NotNull
    private Event event;

    private Boolean isActive = Boolean.TRUE;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
