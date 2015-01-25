/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.boundary;

import business.security.entity.Event;
import business.security.entity.Preference;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * manage the entity preference
 *
 * @author Alessandro
 */
@Stateless
public class PreferenceManager{

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    /**
     * add a preference in the database
     *
     * @param preference
     */
    
    public void addPreference(Preference preference) {
        em.merge(preference);
    }

    /**
     * This function query the database to get the preferences of an event
     *
     * @param event
     * @return a list of preferences
     */
    
    public List<String> getPreferenceOfEvent(Event event) {

        Query query = em
                .createQuery("Select pref.main.conditione FROM Preference pref WHERE pref.event.idEvent.id= :event").setParameter("event", event.getIdEvent().getId());

        List<String> result = new ArrayList<>(query.getResultList());
        return result;
    }
}
