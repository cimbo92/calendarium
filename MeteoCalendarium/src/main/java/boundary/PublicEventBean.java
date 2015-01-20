/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entity.Event;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import control.EventManagerInterface;
import control.PreferenceManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Alessandro De Angelis Bean that manage Public Events
 */
@Named
@RequestScoped

public class PublicEventBean {


    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    private PreferenceManagerInterface pm;
    @EJB
    private EventManagerInterface em;
    @EJB
    private UserEventManagerInterface uem;
    @EJB
    private UserManagerInterface um;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    /**
     * Preferences of Selected Public Event
     */
    private List<String> preferences = new ArrayList<>();
    /**
     * Invitated Users of public Event
     */
    private List<String> invitated = new ArrayList<>();
    /**
     * Seleted Public Event
     */
    private Event event;

    /*
     * ******************************************************************
     *PUBLIC FUNCTIONS
     *******************************************************************
     */
    @PostConstruct
    public void init() {
        event = new Event();
    }

    /**
     * Function called by PrimeFace's schedule in caso of selection of event
     * that load informations about the selected @Event
     *
     * @param selectEvent
     */
    public void onEventSelect(SelectEvent selectEvent) {
        DefaultScheduleEvent selectedEvent = (DefaultScheduleEvent) selectEvent.getObject();
        event = em.loadSpecificEvent(selectedEvent.getDescription());
        List<String> preferenzeEvento = pm.getPreferenceOfEvent(event);

        for (String preferenza : preferenzeEvento) {
            this.preferences.add(preferenza);
        }
        List<String> invitedUsers = uem.invitedUsersOfEvent(event);
        for (String invitedUser : invitedUsers) {
            this.invitated.add(invitedUser);
        }

    }

    /*
     * ******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public String getPublicEvent() {
        if (event.isPublicEvent()) {
            return "public";
        } else {
            return "private";
        }
    }

    public String getOutdoor() {
        if (event.isOutdoor()) {
            return "outdoor";
        } else {
            return "indoor";
        }

    }

    public Date getStartDate() {
        if (event.getStartDate() != null) {
            Date temp = new Date(event.getStartDate().getTime());
            return temp;
        } else {
            return null;
        }
    }

    public Date getEndDate() {
        if (event.getStartDate() != null) {
            Date temp = new Date(event.getEndDate().getTime());
            return temp;
        } else {
            return null;
        }
    }

    public Event getEvent() {
        if (event == null) {
            event = new Event();
        }

        return event;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> selectedPref) {
        this.preferences = selectedPref;
    }

    public List<String> getInvitated() {
        return invitated;
    }

    public void setInvitated(List<String> invitated) {
        this.invitated = invitated;
    }

}
