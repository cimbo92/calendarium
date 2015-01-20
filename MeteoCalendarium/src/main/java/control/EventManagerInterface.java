/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import HelpClasses.OverlappingException;
import entity.Event;
import entity.User;
import java.util.List;
import javax.ejb.Remote;
import javax.persistence.EntityManager;

/**
 *
 * @author home
 */
@Remote
public interface EventManagerInterface {

    public void removeEvent(Event event);

    public void removeEventByID(Event event);

    public Event loadSpecificEvent(String idEvent);

    public boolean isIndoor(Event event);

    public void addEvent(Event event, User user) throws OverlappingException;

    ;

    public List<Event> findInvitatedEvent(User user);

    public List<Event> loadCalendar(User user);

    public List<Event> loadPublicCalendar(String username);

    public void removeAllEvent(User user);

    public List<Event> getEventsCreated(User user);

    public boolean isCreator(Event event, User user);

    public boolean searchOverlapping(Event event, User user);

    public EntityManager getEm();

    public void setEm(EntityManager em);
}
