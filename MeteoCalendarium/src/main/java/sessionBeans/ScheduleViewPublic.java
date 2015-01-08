/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.UserManagerInterface;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Alessandro
 */

@RequestScoped
@Named("scheduleViewPublic")
public class ScheduleViewPublic implements Serializable {
 
    private ScheduleModel eventModel;
   
    @EJB
    private EventManagerInterface em;
    
     @EJB
    private UserManagerInterface um;
     
     private List<User> prova1;

    public List<User> getProva1() {
        return prova1;
    }

    public void setProva1(List<User> prova1) {
        this.prova1 = prova1;
    }
 
    public List<User> getProva2() {
        return prova2;
    }

    public void setProva2(List<User> prova2) {
        this.prova2 = prova2;
    }

     private List<User> prova2;
     
     
private String username;

private List<String> users =new ArrayList<>();

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
     
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();   
        users =  um.getListUsers();
        prova2 = um.userList();
        prova1= new ArrayList<>();
    }
     
    public void loadCalendar(){
        
       // username= prova1.get(0).getEmail();
       // System.out.println(username);
        username="";
        List<Event> tempCalendar = em.loadPublicCalendar(username);
       
        for (Event tempCalendar1 : tempCalendar) {
           DefaultScheduleEvent temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate());
           temp.setDescription(tempCalendar1.getIdEvent().getId().toString());
           if(!eventModel.getEvents().contains(temp)) 
           {
               eventModel.addEvent(temp);
           }
        else
           {
               eventModel.updateEvent(temp);
           } 
       }             
     }	
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     

}


