/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import HelpClasses.EventCreation;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.OneToOne;

/**
 * entity representing an Event with his
 *
 * @author home
 */
@Entity
public class Event implements Serializable {


    /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne
    private IDEvent idEvent;

    @NotNull(message = "May not be empty1")
    @ManyToOne(targetEntity = User.class, optional = false)
    private User creator;

    @NotNull(message = "May not be empty2")
    private String title;

    @NotNull(message = "May not be empty3")
    private Timestamp startDate;

    @NotNull(message = "May not be empty4")
    private Timestamp endDate;

    @NotNull(message = "May not be empty5")
    private String description;

    @NotNull(message = "May not be empty6")
    @ManyToOne(targetEntity = Place.class, optional = false, cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Place place = new Place();

    @NotNull(message = "May not be empty7")

    private boolean outdoor;

    @NotNull(message = "May not be empty8")
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

    public void loadEvent(EventCreation e) {
        this.title = e.getTitle();
        this.creator = e.getCreator();
        this.description = e.getDescription();
        this.idEvent = e.getIdEvent();
        this.outdoor = e.isOutdoor();
        this.place = new Place(e.getPlace());
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place Place) {
        this.place = Place;
    }

    public boolean isOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }

    @Override
    public String toString() {
        return title + "\n" + "Creator: " + creator + "\n" + "Title: " + title + "\n" + "Description: " + description + "\n" + "Place: " + place.getCity() + "\n" + "Starting from: " + startDate + "\n" + "To: " + endDate + "\n" + "OutDoor: " + outdoor + "\n";
    }

}
