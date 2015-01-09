/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.MainCondition;
import entities.Preference;
import entities.UserEvent;
import entities.iDEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.IDEventManagerInterface;
import managerBeans.MailSenderManagerInterface;
import managerBeans.OwmClientInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Alessandro
 */
/**
 *
 * @author home
 */
@Named
@RequestScoped

public class PublicEventBean {

    @EJB
    private PreferenceManagerInterface pm;    
    @EJB
    private EventManagerInterface em;
    @EJB
    private UserEventManagerInterface uem;
    @EJB
    private UserManagerInterface um;
    

    private List<String> preferences = new ArrayList<>();
    private List<String> invitated = new ArrayList<>();
    private Event event;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @PostConstruct
    public void init()
    {
        event=new Event();
    }
    
    public String getPublicEvent() {
        if(event.isPublicEvent())
            return "public";
                    else
            return "private";
    }

    
    public String getOutdoor() {
        if(event.isOutdoor())
            return "outdoor";
        else
            return "indoor";
            
    }


    public Date getStartDate() {
        if(event.getStartDate()!=null)
        {
        Date temp = new Date(event.getStartDate().getTime());
        return temp;
        }
        else 
            return null;
    }


    public Date getEndDate() {
           if(event.getStartDate()!=null)
        {
        Date temp = new Date(event.getEndDate().getTime());
        return temp;
        }else
            return null;
    }
    
    public Event getEvent(){
         if (event == null) {
            event = new Event();      
        }

        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
     
    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> selectedPref) {
        this.preferences = selectedPref;
    }


    public List<String> getInvitated() {
        return invitated;
    }

    public void setInvitated(List<String> invitated) {
        this.invitated = invitated;
    }

    
    public void nothing()
    {
        
    }
    
     public void onEventSelect(SelectEvent selectEvent) {
         DefaultScheduleEvent selectedEvent = (DefaultScheduleEvent) selectEvent.getObject(); 
         event=em.loadSpecificEvent(selectedEvent.getDescription());
         List<String> preferenzeEvento = pm.getPreferenceOfEvent(event);
              
        for (String preferenza : preferenzeEvento) {
            this.preferences.add(preferenza);
        }
         List<String> invitedUsers = uem.invitedUsersOfEvent(event);
        for (String invitedUser : invitedUsers) {
            this.invitated.add(invitedUser);
        }
    
     }
     
     

}
