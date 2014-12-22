/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.Preference;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.EventManager;
import managerBeans.UserManager;

/**
 *
 * @author home
 */
@Named
@RequestScoped

public class EventBean {


    
    @EJB
    private EventManager em;

    private Event event;
    private Preference preference;
    
    public EventBean(){}
    
    public Event getEvent(){
         if (event == null) {
            event = new Event();      
        }
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    
    public void addEvent() {
        
        em.addEvent(event);
    }
}
