package com.wetark.main.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.event.EventVariable;
import com.wetark.main.model.matchedTrade.MatchedTrade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

public class EventRequest {
    private String title;
    private String description;
    private List<String> tags = new ArrayList<>();
    private Map<String, EventVariable> eventVariableMap  = new HashMap<String, EventVariable>();
    private String picture;
    public Date expireAt;

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

    public Map<String, EventVariable> getEventVariableMap() {
        return eventVariableMap;
    }

    public void setEventVariableMap(Map<String, EventVariable> eventVariableMap) {
        this.eventVariableMap = eventVariableMap;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Event createEvent(){
        Event event = new Event();
        event.setTitle(this.title);
        event.setDescription(this.description);
        event.setTags(this.tags);
        event.setEventVariableMap(this.eventVariableMap);
        event.setExpireAt(this.expireAt);
        event.setPicture(this.picture);
        return event;
    }
}
