/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.UserEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import managerBeans.EventManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;

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
    private List<Event> invites;

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
        invites = em.findInvitatedEvent(um.getLoggedUser());
        enableInvitation = true;
        if (invites.isEmpty()) {
            enableInvitation = false;
            Event noInvitation = new Event();
            noInvitation.setTitle("No Invitation");
            invites.add(noInvitation);
        }
    }

    /**
     * User Accept Invitation
     *
     * @param event related @Event
     * @return
     */
    public String acceptInvite(Event event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UserEvent ue = uem.getUserEventofUser(event, um.getLoggedUser());

        //Before Accepting controls that it is possible ( Overlapping)
        if (!em.searchEventOverlapping(event, um.getLoggedUser())) {
            uem.modifyUserEvent(ue, true, true);
            invites.remove(event);
            return "calendar?faces-redirect=true";
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Found Overlapping , Impossible to Accept", null));
            return "";
        }

    }

    /**
     * User Decline Invitation ( will be never seen )
     *
     * @param event
     */
    public void declineInvite(Event event) {
        UserEvent ue = uem.getUserEventofUser(event, um.getLoggedUser());
        uem.modifyUserEvent(ue, false, true);
        invites.remove(event);
        if (invites.isEmpty()) {
            enableInvitation = false;
            Event noInvitation = new Event();
            noInvitation.setTitle("No Invitation");
            invites.add(noInvitation);
        }

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

    public List<Event> getInvites() {
        return invites;
    }

    public void setInvites(List<Event> invites) {
        this.invites = invites;
    }

}
