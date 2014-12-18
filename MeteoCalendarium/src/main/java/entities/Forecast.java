/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author home
 */
public class Forecast implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    
    
    @Id 
    @NotNull(message = "May not be empty")
    private Date date;
    @Id
    @NotNull(message = "May not be empty")
    private Place place;
    @NotNull(message = "May not be empty")
    private Forecast forecast;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    

}
