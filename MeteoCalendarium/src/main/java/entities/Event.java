/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */@Entity
public class Event implements Serializable {
     
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(generator="increment")
    private int idEvent;
    
    @NotNull
    @OneToOne(targetEntity = User.class, optional = false)
    private User creator;
    
    @NotNull(message = "May not be empty")
    private String title;
    
    @NotNull(message = "May not be empty")
    private String date;
    
    @NotNull(message = "May not be empty")
    private String startHour ;
    
    @NotNull(message = "May not be empty")
    private String endHour;
    
    
    private String description;
    
    @NotNull(message = "May not be empty")
    private Place place;
    private boolean outdoor;
    
    @OneToMany(mappedBy = "event")
    private List<Preference> preferences;
    
    @OneToMany(mappedBy = "event")
    private List<Invitation> invitations;
    
    
    
    
    
    
    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place Place) {
        this.place = Place;
    }
    
    public boolean getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }
    
    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }
    
    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }
    
}
