/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.boundary;

import business.security.exception.OverlappingException;
import business.security.entity.Event;
import business.security.entity.Users;
import business.security.entity.UserEvent;
import business.security.entity.IDEvent;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Event Manager
 *
 * @author home
 */
@Stateless
public class EventManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    /**
     * add Event and place if not already saved ,
     *
     * @param event Event to Add
     * @param user Creator
     * @throws OverlappingException if event already in event date
     */

    public void addEvent(Event event, Users user) throws OverlappingException {

        if (this.searchOverlapping(event, user)) {
            throw new OverlappingException();
        } else {
            em.merge(event.getPlace());
            em.persist(event.getIdEvent());
            em.persist(event);
        }

    }

    /**
     * Check if there is overlapping with existing events
     *
     * @param event event to check
     * @param user user to check
     * @return
     */

    public boolean searchOverlapping(Event event, Users user) {

        List<Event> list;

        //  Query query = em.createQuery("SELECT e FROM UserEvent ue JOIN Users u JOIN Event e WHERE u.email = :email AND (ue.creator = 1 OR ue.accepted = 1)").setParameter("email", user.getEmail());
        Query query = em.createQuery("SELECT ue.event FROM UserEvent ue  WHERE (ue.user = :user AND (ue.creator =1 OR ue.accepted = 1))").setParameter("user", user);

        list = query.getResultList();

        for (Event e : list) {
            if (totalOverlapping(e.getStartDate(), e.getEndDate(), event.getStartDate(), event.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    /**
     * return true if user is creator of event
     *
     * @param event
     * @param user
     * @return
     */

    public boolean isCreator(Event event, Users user) {
        Query query = em.createQuery("SELECT ue FROM UserEvent ue WHERE ue.event= :event and ue.user= :user").setParameter("event", event).setParameter("user", user);
        List<UserEvent> result = new ArrayList<>(query.getResultList());
        return result.get(0).isCreator();
    }

    /**
     * remove event and related UserEvent , ID m Preferences
     *
     * @param event
     */

    public void removeEvent(Event event) {

        Query query1 = em.createQuery("Delete From Preference p Where p.event= :event").setParameter(("event"), event);
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue Where ue.event= :event").setParameter(("event"), event);
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e Where e.idEvent.event= :event").setParameter(("event"), event);
        query3.executeUpdate();
        Query query4 = em.createQuery("Delete From IDEvent e Where e.event= :event").setParameter(("event"), event);
        query4.executeUpdate();
    }

    /**
     * return Event with iDEvent
     *
     * @param iDEvent
     * @return
     */

    public Event loadSpecificEvent(String iDEvent) {
        IDEvent id = new IDEvent(Long.parseLong(iDEvent));
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.idEvent =:id").setParameter("id", id);
        List<Event> result = new ArrayList<>(query.getResultList());

        return result.get(0);
    }

    /**
     * return Invitations of Users
     *
     * @param user
     * @return
     */

    public List<Event> findInvitatedEvent(Users user) {

        Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE ue.user.email = :user AND ue.creator=false and ue.viewed=false").setParameter(("user"), user.getEmail());
        List<Event> tempSet = new ArrayList<>(query.getResultList());

        return (List) tempSet;
    }

    /**
     * return Users Events ( Created or Accepted)
     *
     * @param user
     * @return
     */

    public List<Event> loadCalendar(Users user) {

        Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE (ue.user = :user AND (ue.accepted=true OR ue.creator=true))").setParameter(("user"), user);
        List<Event> tempSet = new ArrayList<>(query.getResultList());

        return tempSet;
    }

    /**
     * load Public calendar of the Users
     *
     * @param username
     * @return
     */

    public List<Event> loadPublicCalendar(String username) {

        Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE (ue.user.email = :user AND (ue.accepted=true OR ue.creator=true) AND ue.event.publicEvent=true)").setParameter(("user"), username);
        List<Event> tempSet = new ArrayList<>(query.getResultList());

        return tempSet;
    }

    /**
     * remove all Event of an user for Importing
     *
     * @param user
     */

    public void removeAllEvent(Users user) {
        Query query1 = em.createQuery("Delete From Preference p Where p.event in (Select e From Event e Where e.creator.email = :mail)").setParameter(("mail"), user.getEmail());
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue  Where ue.event in (Select e From Event e Where e.creator.email= :mail)").setParameter(("mail"), user.getEmail());
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e where e.creator.email = :mail").setParameter(("mail"), user.getEmail());
        query3.executeUpdate();
    }

    /**
     * return events created by Users
     *
     * @param user
     * @return
     */

    public List<Event> getEventsCreated(Users user) {

        Query query = em.createQuery("Select e From Event e Where e.creator.email= :mail").setParameter("mail", user.getEmail());
        return query.getResultList();
    }

    public boolean totalOverlapping(Timestamp startFirst, Timestamp endFirst, Timestamp startSecond, Timestamp endSecond) {
        return (startFirst.before(endSecond) || startFirst.equals(endSecond))
                && (startSecond.before(endFirst) || startSecond.equals(endFirst));
    }

    /**
     * return true if event is indoor
     *
     * @param event
     * @return
     */

    public boolean isIndoor(Event event) {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e= :event").setParameter("event", event);
        List<Event> result = new ArrayList<>(query.getResultList());
        return !result.get(0).isOutdoor();
    }


    public EntityManager getEm() {
        return em;
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }


    public void removeEventByID(Event event) {
        Query query1 = em.createQuery("Delete From Preference p Where p.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue Where ue.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e Where e.idEvent.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query3.executeUpdate();
        Query query4 = em.createQuery("Delete From IDEvent e Where e.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query4.executeUpdate();
    }

}
