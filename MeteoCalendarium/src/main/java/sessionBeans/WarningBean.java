/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.Preference;
import entities.UserEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.BadWeatherNotificationManagerInterface;
import managerBeans.EventManagerInterface;
import managerBeans.PreferenceManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class WarningBean {
    
    @EJB
    private BadWeatherNotificationManagerInterface bwnm;
    @EJB
    UserManagerInterface um;
    @EJB
    EventManagerInterface em;
    @EJB
    PreferenceManagerInterface pm;
    @EJB
    UserEventManagerInterface uem;
    
    private List<Event> warnings;
    private List<Timestamp> solutions;

    
     @PostConstruct
    public void init() {
        warnings = new ArrayList<>();
        warnings = bwnm.findWarnings(um.getLoggedUser());
        if(!warnings.isEmpty())
        {
            searchSolution();
        }
        else
            System.out.println(" warning empty");
         
    }
    
    public void searchSolution()
    {
        
        solutions = bwnm.findSolution(warnings);
        
        
    }

    public List<Timestamp> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Timestamp> solutions) {
        this.solutions = solutions;
    }
    
    public List<Event> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Event> warnings) {
        this.warnings = warnings;
    }
   
    public void modifyOk(Event event, EventBean eb) throws OverlappingException
    {
        List<String> preferenceEvent = new ArrayList<>();
        preferenceEvent= pm.getPreferenceOfEvent(event);
        List<String> userEvent = new ArrayList<>();
        userEvent = uem.invitedUsersOfEvent(event);
        
        em.removeEvent(event);
        long diff = event.getEndDate().getTime()-event.getStartDate().getTime();
        System.out.println("Evento modificato: " + event.getTitle() + "diff: " + diff);
        Timestamp help;
        boolean ok=false;
        for(int i=0;i<warnings.size()&&!ok;i++)
        {
            if(warnings.get(i).getTitle().equalsIgnoreCase(event.getTitle()))
                if(solutions.get(i)!=null)
                {
                     event.setStartDate(solutions.get(i));
                     System.out.println(event.getStartDate());
                     help=new Timestamp(0);
                     
                     help.setTime(solutions.get(i).getTime()+diff);
                     event.setEndDate(help);
                     System.out.println("start "+event.getStartDate()+"end "+event.getEndDate());
                     //em.modifyEvent(event);
                    // em.(event);
                     eb.modifyFromWarning(event, preferenceEvent, userEvent);
                     
                     ok=true;
                     warnings.remove(i);
                }
            
        }
        
        
    }
    
    public String getDate(String title)
    {
        for(int i=0;i<warnings.size();i++)
        {
            if(warnings.get(i).getTitle().equalsIgnoreCase(title))
                if(solutions.get(i)!=null)
                    return solutions.get(i).toString();
        }
        return "no possible postpone in the next 10 days";
    }
    
    
}
