/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
public class Forecast implements Serializable {
    
    private static final long serialVersionUID = 1L;  
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne(targetEntity = Place.class, optional = false)
    private Place place;
    
    @ManyToOne(targetEntity = MainCondition.class, optional = false)
    private MainCondition mainCondition;
    
    @NotNull(message = "May not be empty")
    private String date;
    
    @NotNull(message = "May not be empty")
    private Forecast forecast;

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public MainCondition getMainCondition() {
        return mainCondition;
    }

    public void setMainCondition(MainCondition mainCondition) {
        this.mainCondition = mainCondition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof Forecast)){
            return false;
        }
        Forecast forecast = (Forecast) obj;
        if(id == forecast.getId()){
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return id;
    }
}
