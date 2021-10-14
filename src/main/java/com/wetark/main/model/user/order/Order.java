package com.wetark.main.model.user.order;

import com.wetark.main.model.Base;
import com.wetark.main.model.user.balance.Balance;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(	name = "orders" )
public class Order extends Base {
    @Id
    @GeneratedValue(generator = generatorName)
    @GenericGenerator(name = generatorName, strategy = "uuid")
    private String id;

    @NotBlank
    @OneToOne
    private Balance balance;

    @NotBlank
    @Enumerated
    private OrderType type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
