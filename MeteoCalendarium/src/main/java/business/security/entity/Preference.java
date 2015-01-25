/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Alessandro
 */
/**
 *
 * @author home
 */
@Entity
public class Preference implements Serializable {

    /*
     *******************************************************************
     * FIELDS
     *******************************************************************
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(targetEntity = Event.class, optional = false, cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(targetEntity = MainCondition.class, optional = false, cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private MainCondition main = new MainCondition();

    /*
     *******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    public Preference(Event ev, String pref) {
        main.setCondition(pref);
        this.setEvent(ev);
    }

    public Preference() {
    }


    /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public MainCondition getMain() {
        return main;
    }

    public void setMain(MainCondition main) {
        this.main = main;
    }

}
