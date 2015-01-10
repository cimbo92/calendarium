/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.Place;
import entities.Preference;
import entities.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
public interface EventManagerInterface {
       
    //TODO
    public void modifyEvent(Event event);

    public void removeEvent(Event event);
    
    public Event loadSpecificEvent(String idEvent);
    
    public List<Event> getAllUserEvent(String idUser);
    
    public List<Event> searchEventIndoorUser(String idUser);
    
    public List<Event> searchOutdoorEvent(String idUser);
    
    //Questo metodo da tutti gli utenti che siano invitati ad un evento
    public List<User> getInvitedUsers(Event event);
    
    public boolean searchEventOverlapping(Event event);

    public void addEvent(Event event) throws OverlappingException;;
    
    public List<Event> findInvitatedEvent(User user);
    
     public List<Event> loadCalendar(User user);
     
     public List<Event> loadPublicCalendar(String username);
   
     public List<Event> loadPublicCalendar(User user);
     
     public void removeAllEvent(User user);
   
     
}
