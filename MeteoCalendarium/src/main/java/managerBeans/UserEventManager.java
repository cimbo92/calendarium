/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import entities.UserEvent;
import java.security.Principal;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author alessandro
 */
@Stateless
public class UserEventManager implements UserEventManagerInterface {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    
    @Override
    public void addUserEvent(UserEvent userEvent) {
        em.merge(userEvent);
    }
    

    
    @Override
    public User findEventCreator(Event event) {
      Query query;
        query =em.createQuery( "SELECT u FROM User u Join UserEvent ue WHERE ue.event= :event and ue.creator=1" ).setParameter("event", event);      
        List<User> user =query.getResultList(); 
       return user.get(0);
           
    }

    @Override
    public UserEvent getUserEventofUser(Event event,User user) {
        Query query;
        query =em.createQuery( "SELECT ue FROM UserEvent ue WHERE ue.event= :event and ue.creator=0 and ue.user = :user  " ).setParameter("event", event).setParameter("user",user);
        List<UserEvent> result =query.getResultList(); 
    return result.get(0);
    }

    
    @Override
    public void modifyUserEvent(UserEvent userEvent,boolean decision) {
         Query query =em
                    .createQuery("UPDATE UserEvent ue SET ue.accepted = :decision , ue.view = :view"
                            + " WHERE ue= :userEvent");
            query.setParameter("userEvent", userEvent);
            query.setParameter("decision", decision);
            query.setParameter("view",true);
            query.executeUpdate();
    }
    
}
