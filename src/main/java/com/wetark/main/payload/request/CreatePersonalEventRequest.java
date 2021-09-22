package com.wetark.main.payload.request;

import com.wetark.main.model.event.EventVariable;

import java.util.HashMap;
import java.util.Map;

public class CreatePersonalEventRequest {
    private String eventId;
    private String title;
    private Map<String, EventVariable> eventVariableMap;

    public CreatePersonalEventRequest() {
    }

    public CreatePersonalEventRequest(String title, Map<String, EventVariable> eventVariableMap) {
        this.title = title;
        this.eventVariableMap = eventVariableMap;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, EventVariable> getEventVariableMap() {
        return eventVariableMap;
    }

    public void setEventVariableMap(Map<String, EventVariable> eventVariableMap) {
        this.eventVariableMap = eventVariableMap;
    }
}
