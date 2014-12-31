/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Time;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

/**
 *
 * @author home
 */@Entity
public class Event implements Serializable {
     
    private static final long serialVersionUID = 1L;

    @Id
    @OneToOne(targetEntity = iDEvent.class, optional = false, cascade={CascadeType.MERGE},fetch=FetchType.LAZY)
    private iDEvent idEvent;
    
    @NotNull(message = "May not be empty")
    private String title;
    
    @NotNull(message = "May not be empty")
    private Timestamp startDate;

     @NotNull(message = "May not be empty")
    private Timestamp endDate;

    public Timestamp getStartDate() {
        return startDate;
    }

    public void convertStartDate(Date date)
    {
     startDate = new java.sql.Timestamp(date.getTime());        
    }
    
    public void convertEndDate(Date date)
    {
       endDate = new java.sql.Timestamp(date.getTime());      
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

    private String description;
    
    @NotNull(message = "May not be empty")
    @ManyToOne(targetEntity = Place.class, optional = false, cascade={CascadeType.MERGE},fetch=FetchType.EAGER)
    private Place place = new Place();
       
   
    private boolean outdoor;
    
    public iDEvent getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(iDEvent idEvent) {
        this.idEvent = idEvent;
    }
    
    public String getTitle() {
        return title;
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
    
    public boolean getOutdoor() {
        return outdoor;
    }

    public void setOutdoor(boolean outdoor) {
        this.outdoor = outdoor;
    }

    
}
