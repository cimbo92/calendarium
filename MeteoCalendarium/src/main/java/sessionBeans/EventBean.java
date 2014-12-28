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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserManager;

/**
 *
 * @author home
 */
@Named
@RequestScoped

public class EventBean {


    @EJB
    private PreferenceManagerInterface pm;    
    @EJB
    private EventManagerInterface em;
    @EJB
    private UserManager um;

    
    
    private List<String> selectedPref = new ArrayList<>();
    private List<Preference> preferences = new ArrayList<>();
    private Event event;
    
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
        
        try{
            em.addEvent(um.getLoggedUser().getEmail(), event);
        }catch (OverlappingException e ){
               System.out.println("Errore in add event di EventManager");
        }
    }
    
    public void create(){
        this.addEvent();
        this.save();
    }
      

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }
    
    
    public List<String> getSelectedPref() {
        return selectedPref;
    }

    public void setSelectedPref(List<String> selectedPref) {
        this.selectedPref = selectedPref;
    }

    public void save()
    { 
        for(int i=0;i<selectedPref.size();i++)
        {
            preferences.add(new Preference(event,selectedPref.get(i)));
            pm.addPreference(preferences.get(i));
        }
    }
    
    public List<String> listPref ()
    {
        return MainCondition.getListPref();
    }

}
