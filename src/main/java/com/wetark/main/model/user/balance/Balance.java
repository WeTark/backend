package com.wetark.main.model.user.balance;

import com.wetark.main.model.Base;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.math.BigDecimal;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
public class Balance extends Base {
    @Id
    @GeneratedValue(generator = generatorName)
    @GenericGenerator(name = generatorName, strategy = "uuid")
    private String id;

    private BigDecimal availableBalance = BigDecimal.valueOf(1000);
    private BigDecimal onHoldBalance = BigDecimal.valueOf(0);
    private BigDecimal investedBalance = BigDecimal.valueOf(0);

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getOnHoldBalance() {
        return onHoldBalance;
    }

    public void setOnHoldBalance(BigDecimal onHoldBalance) {
        this.onHoldBalance = onHoldBalance;
    }

    public BigDecimal getInvestedBalance() {
        return investedBalance;
    }

    public void setInvestedBalance(BigDecimal investedBalance) {
        this.investedBalance = investedBalance;
    }
}
