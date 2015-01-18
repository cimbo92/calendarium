/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * entity that rapresent a forecast
 * @author home
 */
@Entity
public class Forecast implements Serializable {

     /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = Place.class, optional = false)
    private Place place;

    @ManyToOne(targetEntity = MainCondition.class, optional = false)
    private MainCondition mainCondition;

    @NotNull(message = "May not be empty4")
    private Timestamp date;

     /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */


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

    @Override
    public String toString() {
        return "Forecast{" + "mainCondition=" + mainCondition + '}';
    }

    public void setMainCondition(MainCondition mainCondition) {
        this.mainCondition = mainCondition;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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
