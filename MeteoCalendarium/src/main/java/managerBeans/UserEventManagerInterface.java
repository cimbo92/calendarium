/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import entities.UserEvent;
import javax.ejb.Remote;

/**
 *
 * @author alessandro
 */
@Remote
public interface UserEventManagerInterface {
    
    public void addUserEvent(UserEvent userEvent);
    
    public User findEventCreator(Event event);
    
}
