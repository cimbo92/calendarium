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
        invites = new ArrayList<>();
        invites = em.findInvitatedEvent(um.getLoggedUser());
        invites.add(new Event());
    }

    public void acceptInvite(Event event) {
       UserEvent ue =uem.getUserEventofUser(event, um.getLoggedUser());
     uem.modifyUserEvent(ue,true);
    }

    public void declineInvite(Event event) {
        UserEvent ue =uem.getUserEventofUser(event, um.getLoggedUser());
     uem.modifyUserEvent(ue,false);
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
