/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
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
        if(!solutions.isEmpty())
        System.out.println("Va: " + solutions.get(0).toString());
        
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
    
    public void modify(Event event) {
       
        System.out.println("Evento modificato: " + event.getTitle());
        int year, month, day;
        int index=-1;
        System.out.println("Dim: " + warnings.size() + "  " + solutions.size());
        for(int i=0;i<warnings.size()&&(index!=-1);i++)
        {
            if(event.getIdEvent()==warnings.get(i).getIdEvent())
            {
                index=i;
                event.setStartDate(solutions.get(i));
                System.out.println("Data nuova: " + solutions.get(i));
                
            }
        }
        em.modifyEvent(event);
    }
    public void modifyOk(Event event)
    {
        System.out.println("Evento modificato: " + event.getTitle());
        long diff = event.getEndDate().getTime()-event.getStartDate().getTime();
        Timestamp help;
        boolean ok=false;
        for(int i=0;i<warnings.size()&&!ok;i++)
        {
            if(warnings.get(i).getTitle().equalsIgnoreCase(event.getTitle()))
                if(solutions.get(i)!=null)
                {
                     event.setStartDate(solutions.get(i));
                     help=solutions.get(i);
                     help.setTime(solutions.get(i).getTime()+diff);
                     event.setEndDate(help);
                     em.modifyEvent(event);
                     ok=true;
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
