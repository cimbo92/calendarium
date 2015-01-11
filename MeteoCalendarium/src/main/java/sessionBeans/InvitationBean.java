/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.User;
import entities.UserEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
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
@RequestScoped
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
        invites = new ArrayList<>();
        loadInvites();
    }

    
    public void loadInvites()
    {
             invites = em.findInvitatedEvent(um.getLoggedUser());
             if(invites.isEmpty())
             {
                 Event noInvitation = new Event();
                 noInvitation.setTitle("No Invitation");
                 invites.add(noInvitation);
             }
    }
    
    public void acceptInvite(Event event) {
          FacesContext context = FacesContext.getCurrentInstance();
       UserEvent ue =uem.getUserEventofUser(event, um.getLoggedUser());
      
         
       
       
     uem.modifyUserEvent(ue,true);
     invites.remove(event);
    }

    public void declineInvite(Event event) {
    UserEvent ue =uem.getUserEventofUser(event, um.getLoggedUser());
    uem.modifyUserEvent(ue,false);
    invites.remove(event);
    }

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
