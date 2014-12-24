/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import HelpClasses.Date;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
@Entity
public class Invitation implements Serializable {
    
     private static final long serialVersionUID = 1L;

   @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne(targetEntity = User.class, optional = true)
    private User owner;
    
    @ManyToOne(targetEntity = Event.class, optional = true)
    private Event event;
    
    private String date;
    
    private String description;
    
    private boolean accepted;
    
    private boolean seen;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Invitation)){
            return false;
        }
        Invitation invitation = (Invitation) obj;
        if(id == invitation.getId()){
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
}
