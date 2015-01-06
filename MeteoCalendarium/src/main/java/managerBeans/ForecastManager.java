/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.Forecast;
import entities.Place;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author home
 */
@Stateless
@Remote(ForecastManagerInterface.class)
public class ForecastManager implements ForecastManagerInterface {
    
    @PersistenceContext
    private EntityManager em;

    @Override
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
    
    
}
