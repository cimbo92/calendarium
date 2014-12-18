/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import static javax.ws.rs.client.Entity.entity;

/**
 *
 * @author home
 */@Entity
public class Event implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    private int idEvent;
    @NotNull(message = "May not be empty")
    private String title;
//    @NotNull(message = "May not be empty")
//    private Date startDate ;
//    @NotNull(message = "May not be empty")
//    private Date endDate;
    @NotNull(message = "May not be empty")
    private String description;
    @NotNull(message = "May not be empty")
    private String place;

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String Title) {
        this.title = Title;
    }

//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }

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
    
    
}
