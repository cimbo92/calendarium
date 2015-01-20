/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelpClasses;

import entity.Event;
import entity.IDEvent;
import entity.User;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Alessandro
 */
public class EventCreation {

    private IDEvent idEvent = new IDEvent();

    private User creator = new User();

    private String title;

    private Timestamp startDate;

    private Timestamp endDate;

    private String description;

    private String place;

    private boolean outdoor;

    private boolean publicEvent;

    /*
     *******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * convert date in Timestamp
     *
     * @param date
     */
    public void convertStartDate(Date date) {
        startDate = new java.sql.Timestamp(date.getTime());
    }

    /**
     * convert date in Timestamp
     *
     * @param date
     */
    public void convertEndDate(Date date) {
        endDate = new java.sql.Timestamp(date.getTime());
    }

     public void loadEvent(Event e) {
        this.title = e.getTitle();
        this.creator = e.getCreator();
        this.description = e.getDescription();
        this.idEvent = e.getIdEvent();
        this.outdoor = e.isOutdoor();
        this.place = e.getPlace().getCity();
        this.publicEvent = e.isPublicEvent();
        this.startDate = e.getStartDate();
        this.endDate = e.getEndDate();
    }


    /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public IDEvent getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(IDEvent idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        {
            return title;
        }
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String Place) {
        this.place = Place;
    }

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }


}
