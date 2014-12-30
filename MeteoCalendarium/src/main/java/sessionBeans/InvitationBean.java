/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.EventManager;
import managerBeans.EventManagerInterface;
import managerBeans.UserEventManagerInterface;
import managerBeans.UserManagerInterface;

/**
 *
 * @author Alessandro
 */
@Named
@RequestScoped

public class InvitationBean {

    @EJB
    EventManagerInterface em;
    @EJB
    UserManagerInterface um;
    @EJB
    UserEventManagerInterface uem;

    private List<Event> invites = new ArrayList<>();

    public List<Event> getInvites() {
        return invites;
    }

    public int getI() {
        return i;
    }

    public void setInvites(List<Event> invites) {
        this.invites = invites;
    }
    private int i=0;

    public void next() {
        if (i < invites.size()-1) {
            i++;
        } else {
            i = 0;
        }
    }

    public void prev() {
        if (i != 0) {
            i--;
        }else
        {
            i=invites.size()-1;
        }
    }

    public void loadList() {
        i = 0;
        invites = em.findInvitatedEvent(um.getLoggedUser());

        System.out.println("porcodio");
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

}

