/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 *
 * @author home
 */

public class BadWeatherNotification implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne(targetEntity = User.class, optional = false)
    private User owner;
    
    @OneToOne( targetEntity = Event.class, optional = false)
    private Event event;    
    
    private String description;
    
    @NotNull
    private String date;

    @NotNull
    private boolean dontCare;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
      public boolean isDontCare() {
        return dontCare;
    }

    public void setDontCare(boolean dontCare) {
        this.dontCare = dontCare;
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof BadWeatherNotification)){
            return false;
        }
        BadWeatherNotification badNotification = (BadWeatherNotification) obj;
        if(id == badNotification.getId()){
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
