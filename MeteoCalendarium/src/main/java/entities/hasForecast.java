/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author alessandro
 */
@Entity
public class hasForecast implements Serializable {
    
    private static final long serialVersionUID = 1L;

      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
      private int id;
      
      @ManyToOne(targetEntity = Place.class, optional = false , cascade={CascadeType.MERGE},fetch=FetchType.LAZY)
      private Place place;
      
      @ManyToOne(targetEntity = MainCondition.class, optional = false , cascade={CascadeType.MERGE}, fetch=FetchType.LAZY )
      private MainCondition main= new MainCondition();
      
      private Timestamp date;
      
      public hasForecast(){}

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public MainCondition getMain() {
        return main;
    }

    public void setMain(MainCondition main) {
        this.main = main;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
      
    
}
