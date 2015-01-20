/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entity.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import control.EventManagerInterface;
import control.UserManagerInterface;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 * Manager of Public View
 *
 * @author Alessandro De Angelis
 */
@ViewScoped
@Named
public class ScheduleViewPublic implements Serializable {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    EventManagerInterface em;
    @EJB
    UserManagerInterface um;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    private List<String> users = new ArrayList<>();
    private Event event;

    private ScheduleModel eventModel;

    private String username;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        users = um.getListUsersPublic();
    }

    /**
     * load Events on public Schedule
     */
    public void loadCalendar() {

        List<Event> tempCalendar = em.loadPublicCalendar(username);
        for (Event tempCalendar1 : tempCalendar) {
            DefaultScheduleEvent temp = new DefaultScheduleEvent(tempCalendar1.getTitle(), tempCalendar1.getStartDate(), tempCalendar1.getEndDate());
            temp.setDescription(tempCalendar1.getIdEvent().getId().toString());
            if (!eventModel.getEvents().contains(temp)) {
                eventModel.addEvent(temp);
            } else {
                eventModel.updateEvent(temp);
            }

        }

    }

    /*
     * ******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getUsers() {
        return users;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
