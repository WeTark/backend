package com.wetark.main.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wetark.main.model.Base;
import com.wetark.main.model.matchedTrade.MatchedTrade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(	name = "event",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "title")
        })
public class Event extends Base {
    @Id
    @GeneratedValue(generator = generatorName)
    @GenericGenerator(name = generatorName, strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String description;

    @ElementCollection
    private List<String> tags = new ArrayList<>();


    private BigDecimal yesPrice = BigDecimal.valueOf(5);
    private BigDecimal noPrice = BigDecimal.valueOf(5);

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<MatchedTrade> matchedTrade;

    @Temporal(TemporalType.TIMESTAMP)
    public Date expireAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public Set<MatchedTrade> getMatchedTrade() {
        return matchedTrade;
    }

    public void setMatchedTrade(Set<MatchedTrade> matchedTrade) {
        this.matchedTrade = matchedTrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}
