/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Users;
import entity.UserEvent;
import java.util.List;
import javax.ejb.Remote;
import javax.persistence.EntityManager;

/**
 *
 * @author alessandro
 */
@Remote
public interface UserEventManagerInterface {

    public void addUserEvent(UserEvent userEvent);

    public Users findEventCreator(Event event);

    public UserEvent getUserEventofUser(Event event, Users user);

    public void modifyUserEvent(UserEvent userEvent, boolean decision, boolean view);

    public List<String> invitedUsersOfEvent(Event event);

    public void deleteUserEvent(Event event);

    public List<Users> getUsersCreator();

    public List<Users> getInvitedWhoAccepted(Event event);
    
    public void setEm(EntityManager em);
    
    public EntityManager getEm();
}
