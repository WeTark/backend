package com.wetark.main.model.event.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Tag extends Base {
    @Id
    @GeneratedValue(generator = generatorName)
    @GenericGenerator(name = generatorName, strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    private String imageUrl;
    private Boolean isHighlight = false;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Event> events = new HashSet<>();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getHighlight() {
        return isHighlight;
    }

    public void setHighlight(Boolean highlight) {
        isHighlight = highlight;
    }
}
