/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Preference;
import entity.IDEvent;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alessandro
 */
@Stateless
@Remote(PreferenceManagerInterface.class)
public class PreferenceManager implements PreferenceManagerInterface {
 
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    
    @Override
    public void addPreference(Preference preference) {
        em.merge(preference);
    }

    @Override
    public List<String> getPreferenceOfEvent(Event event) {
   
     Query query =em
                    .createQuery("Select pref.main.conditione FROM Preference pref WHERE pref.event= :event").setParameter("event", event);
       
    List<String> result = new ArrayList<>(query.getResultList());

    
    
    
    
    return result; }


    
    
    
    
}
