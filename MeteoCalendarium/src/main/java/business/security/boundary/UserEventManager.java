/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.boundary;

import business.security.entity.Event;
import business.security.entity.Users;
import business.security.entity.UserEvent;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * manager of entity UserEvent
 *
 * @author alessandro
 */
@Stateless
public class UserEventManager  {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    public void addUserEvent(UserEvent userEvent) {
        em.merge(userEvent);
    }

    /**
     * this funtion searches who is the creator of an event
     *
     * @param event
     * @return the creator
     */

    public Users findEventCreator(Event event) {
        Query query;
        query = em.createQuery("SELECT ue.user From UserEvent ue WHERE ue.event= :event and ue.creator=true").setParameter("event", event);
        List<Users> user = query.getResultList();
        return user.get(0);

    }

    /**
     * this funtion return the tuple in UserEvent entity that match with the
     * passed parametes
     *
     * @param event
     * @param user
     * @return
     */

    public UserEvent getUserEventofUser(Event event, Users user) {
        Query query;
        query = em.createQuery("SELECT ue FROM UserEvent ue WHERE ue.event.idEvent.id= :event and ue.creator=0 and ue.user.email = :user  ").setParameter("event", event.getIdEvent().getId()).setParameter("user", user.getEmail());
        List<UserEvent> result = query.getResultList();
        return result.get(0);
    }

    /**
     * this funtion modifies a tuple in UserEvent setting new values
     *
     * @param userEvent
     * @param decision
     * @param view
     */

    public void modifyUserEvent(UserEvent userEvent, boolean decision, boolean view) {

        Query query = em
                .createQuery("UPDATE UserEvent ue SET ue.accepted = :decision , ue.viewed = :viewed Where ue.idUserEvent= :userEvent");
        query.setParameter("userEvent", userEvent.getIdUserEvent());
        query.setParameter("decision", decision);
        query.setParameter("viewed", view);
        query.executeUpdate();
    }

    /**
     * this function searches for invitation of an event
     *
     * @param event
     * @return a list of emails corresponding to invitees
     */

    public List<String> invitedUsersOfEvent(Event event) {
        Query query;
        query = em.createQuery("SELECT ue.user.email FROM UserEvent ue WHERE ue.event= :event and ue.creator=0").setParameter("event", event);

        List<String> result = new ArrayList<>(query.getResultList());

        return result;
    }

    /**
     * this funcion deletes all tuples that have as event the passed parameter
     *
     * @param event
     */

    public void deleteUserEvent(Event event) {
        Query query2 = em.createQuery("Delete From UserEvent ue Where ue.event= :event").setParameter(("event"), event);
        query2.executeUpdate();
    }

    /**
     *
     * @return all users that have created at least an event
     */

    public List<Users> getUsersCreator() {

        Query query = em.createQuery("Select distinct ue.user From UserEvent ue Where ue.creator=1");
        return query.getResultList();

    }

    /**
     *
     * @param event
     * @return a list of users that have accepted the invitation for the passed
     * event
     */

    public List<Users> getInvitedWhoAccepted(Event event) {

        Query query = em.createQuery("Select ue.user From UserEvent ue Where ue.event.idEvent.id= :event and ue.accepted=1").setParameter("event", event.getIdEvent().getId());
        return query.getResultList();

    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
