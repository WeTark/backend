package com.wetark.main.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wetark.main.model.Base;
import com.wetark.main.model.event.tag.Tag;
import com.wetark.main.model.matchedTrade.MatchedTrade;
import com.wetark.main.model.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(	name = "event"
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "title")
//        }
        )
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

    private String sourceOfSettlement;

    private String rcChannelId;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "events")
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Map<String, EventVariable> eventVariableMap  = new HashMap<String, EventVariable>();

    private BigDecimal yesPrice = BigDecimal.valueOf(5);
    private BigDecimal noPrice = BigDecimal.valueOf(5);

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<MatchedTrade> matchedTrade;

    @JsonIgnore
    @OneToOne
    private User user;

    private Boolean isPrivate = false;

    @Column(nullable = true)
    private String picture;

    @Temporal(TemporalType.TIMESTAMP)
    public Date startAt;

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

    public Map<String, EventVariable> getEventVariableMap() {
        return eventVariableMap;
    }

    public void setEventVariableMap(Map<String, EventVariable> eventVariableMap) {
        this.eventVariableMap = eventVariableMap;
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

    public String titleWithVariable() {
        return this.title;
    }

    public String getTitle() {
        this.eventVariableMap.keySet().forEach(key->{
            this.title = this.title.replaceAll("\\$"+key+"\\$", eventVariableMap.get(key).getValue());
        });
        return this.title;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRcChannelId() {
        return rcChannelId;
    }

    public void setRcChannelId(String rcChannelId) {
        this.rcChannelId = rcChannelId;
    }

    public String getSourceOfSettlement() {
        return sourceOfSettlement;
    }

    public void setSourceOfSettlement(String sourceOfSettlement) {
        this.sourceOfSettlement = sourceOfSettlement;
    }
}
