/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.EventCreation;
import entity.Event;
import entity.UserEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import control.EventManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;

/**
 * Bean that manage Invitations
 * @author Alessandro
 */
@RequestScoped
@Named

public class InvitationBean implements Serializable {

    /*
     * ******************************************************************
     * ;MANAGERS
     *******************************************************************
     */
    @EJB
    EventManagerInterface em;
    @EJB
    UserManagerInterface um;
    @EJB
    UserEventManagerInterface uem;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */

    /**
     * Event which user is invited
     */
    private List<EventCreation> invites;

    //boolean used for enable/disable buttons
    private boolean enableInvitation = false;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */

    /**
     * Initialization
     */
    @PostConstruct
    public void init() {
        invites = new ArrayList<>();
        loadInvites();
    }

    /**
     * query database and load Invitations
     */
    public void loadInvites() {
        List<Event> events = em.findInvitatedEvent(um.getLoggedUser());
        for(int i=0;i<events.size();i++)
        {
            EventCreation temp = new EventCreation();
            temp.loadEvent(events.get(i));
            invites.add(temp);
        }
        enableInvitation = true;
        if (invites.isEmpty()) {
            enableInvitation = false;
            EventCreation noInvitation = new EventCreation();
            noInvitation.setTitle("No Invitation");
            invites.add(noInvitation);
        }
    }

    /**
     * User Accept Invitation
     *
     * @param eventC
     * @return
     */
    public String acceptInvite(EventCreation eventC) {
        FacesContext context = FacesContext.getCurrentInstance();
        Event event = new Event();
        event.loadEvent(eventC);
        UserEvent ue = uem.getUserEventofUser(event, um.getLoggedUser());

        //Before Accepting controls that it is possible ( Overlapping)
        if (!em.searchOverlapping(event, um.getLoggedUser())) {
            uem.modifyUserEvent(ue, true, true);
            invites.remove(eventC);
          return "calendar?faces-redirect=true";

        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Found Overlapping , Impossible to Accept", null));
            return"";
        }
    }

    /**
     * User Decline Invitation ( will be never seen )
     *
     * @param eventC
     * @return
     */
    public String declineInvite(EventCreation eventC) {
        Event event = new Event();
        event.loadEvent(eventC);
        UserEvent ue = uem.getUserEventofUser(event, um.getLoggedUser());
        uem.modifyUserEvent(ue, false, true);
        invites.remove(eventC);
        if (invites.isEmpty()) {
            enableInvitation = false;
             EventCreation noInvitation = new EventCreation();
            noInvitation.setTitle("No Invitation");
            invites.add(noInvitation);
        }
return "calendar?faces-redirect=true";
    }

    /*
     *******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public boolean isEnableInvitation() {
        return enableInvitation;
    }

    public void setEnableInvitation(boolean enableInvitation) {
        this.enableInvitation = enableInvitation;
    }

    public List<EventCreation> getInvites() {
        return invites;
    }

    public void setInvites(List<EventCreation> invites) {
        this.invites = invites;
    }

}
