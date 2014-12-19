/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 *
 * @author home
 */

public class BadWeatherNotification implements Serializable {
    
    //Primary key
    //TODO
    @Id User owner;
    @Id Event event;
    
    private String description;
    
    @Temporal(DATE)
    private Date date;

    @NotNull
    private boolean dontCare;

    
    
    
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
      public boolean isDontCare() {
        return dontCare;
    }

    public void setDontCare(boolean dontCare) {
        this.dontCare = dontCare;
    }
    
}
