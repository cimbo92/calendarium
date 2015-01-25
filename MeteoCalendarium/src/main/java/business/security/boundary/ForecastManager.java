/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.boundary;

import business.security.entity.Event;
import business.security.entity.Forecast;
import business.security.entity.Place;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Forecast Manager
 *
 * @author home
 */
@Stateless
public class ForecastManager  {

    @PersistenceContext
    private EntityManager em;

    /**
     * Return Forecast of A place
     *
     * @param place
     * @return
     */
    
    public List<Forecast> getForecastInPlace(Place place) {

        List<Forecast> list;

        try {
            Query query = em.createQuery("SELECT f FROM Forecast f JOIN f.place p WHERE p.city =:city");

            list = query.setParameter("city", place.getCity()).getResultList();

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Errore query getForecastInPlace");
            return null;
        }

        return list;
    }

    /**
     * return Forecast of an Event
     *
     * @param event
     * @return
     */
    
    public List<Forecast> getForecastOfEvent(Event event) {

        Query query = em.createQuery("SELECT distinct f FROM Forecast f JOIN f.place p WHERE p.city =:city and CAST(f.date AS DATE) between  CAST(:start AS DATE) and  CAST(:end AS DATE)").setParameter("city", event.getPlace().getCity());
        query.setParameter("start", event.getStartDate());
        query.setParameter("end", event.getEndDate());
        List<Forecast> result = new ArrayList<>(query.getResultList());
        return result;

    }

}
