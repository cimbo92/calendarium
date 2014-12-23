/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
public interface EventManagerInterface {
   
    public void addEvent(Event event);
    
    public void updateEvent(Event event);
    
    public boolean removeEvent(int ID);
    
    public List<Event> getAllUserEvent(User creator);
    
    public List<Event> searchEventIndoor();
    
    public List<Event> searchOutdoorEvent();
    
    
   
}
