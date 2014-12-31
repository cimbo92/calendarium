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
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import managerBeans.EventManager;
import managerBeans.EventManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;

/**
 *
 * @author Alessandro
 */

@ViewScoped
@Named
public class InvitationBean implements Serializable {

    @EJB
    EventManagerInterface em;
    @EJB
    UserManagerInterface um;
    @EJB
    UserEventManagerInterface uem;

    private List<Event> invites;

    private Event selectedEvent;
    
    @PostConstruct
    public void init() {
        invites=new ArrayList<Event>();
       invites = em.findInvitatedEvent(um.getLoggedUser());
       invites.add(new Event());
       
    }
    /*
  
    public void next() {
          loadList();
          if (i < invites.size()-1) {
          i++;
        } else {
            i = 0;
        }
          loadStrings();
          System.out.println(this.title+this.where);
    }

    public void prev() {
        loadList();
        if (i != 0) {
            i--;
        }else
        {
            i=invites.size()-1;
        }
        loadStrings();
System.out.println(this.title+this.where);
    }

    public void loadList() {
     i=0;
     System.out.println(i+"-"+invites.size());
     invites = em.findInvitatedEvent(um.getLoggedUser());
     System.out.println(i+"-"+invites.size());
     loadStrings();
    }

    
    
    public String getCurrentTitle() {
        if (!invites.isEmpty()) {
            return invites.get(i).getTitle();
        } else {
            return "NoInvitation";
        }
    }

    public String getCurrentDescription() {
        if (!invites.isEmpty()) {

            return invites.get(i).getDescription();
        } else {
            return "";
        }
    }

    public String getCurrentWhen() {
        if (!invites.isEmpty()) {

            return invites.get(i).getStartDate().toString() + " - " + invites.get(i).getEndDate().toString();
        } else {
            return "";
        }
    }

    public String getCurrentWhere() {
        if (!invites.isEmpty()) {

            return invites.get(i).getPlace().getCity();
        } else {
            return "";
        }
    }

    public String getCurrentInvitor() {
        if (!invites.isEmpty()) {
            User creator = uem.findEventCreator(invites.get(i));
            return creator.getEmail();
        } else {
            return "";
        }
    }
      */

    public List<Event> getInvites() {
        return invites;
    }

    public void setInvites(List<Event> invites) {
        this.invites = invites;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}

