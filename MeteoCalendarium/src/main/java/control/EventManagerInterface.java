/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import HelpClasses.OverlappingException;
import entity.Event;
import entity.Users;
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

    public void addEvent(Event event, Users user) throws OverlappingException;

    ;

    public List<Event> findInvitatedEvent(Users user);

    public List<Event> loadCalendar(Users user);

    public List<Event> loadPublicCalendar(String username);

    public void removeAllEvent(Users user);

    public List<Event> getEventsCreated(Users user);

    public boolean isCreator(Event event, Users user);

    public boolean searchOverlapping(Event event, Users user);

    public EntityManager getEm();

    public void setEm(EntityManager em);
}
