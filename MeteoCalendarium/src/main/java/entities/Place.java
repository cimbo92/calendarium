/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
@Entity
public class Place implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
//    @NotNull(message = "May not be empty")
//    private String postalCode;
    @NotNull(message = "May not be empty")
    private String city;
    
    @OneToMany(targetEntity = Event.class, mappedBy = "place")
    private List<Event> events;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getPostalCode() {
//        return postalCode;
//    }
//
//    public void setPostalCode(String postalCode) {
//        this.postalCode = postalCode;
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
    
     @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Place)){
            return false;
        }
        Place place = (Place) obj;
        if(id == place.getId()){
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
    
    
}
