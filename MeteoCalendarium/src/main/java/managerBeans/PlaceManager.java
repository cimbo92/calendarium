/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Place;
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
@Remote(PlaceManagerInterface.class)
public class PlaceManager implements PlaceManagerInterface {
    
     @PersistenceContext
    private EntityManager em;
    
    /**
     *
     * @return
     */
    @Override
    public List<Place> getAllPlaces(){
        
        try{
        Query query = em.createQuery("SELECT * FROM Place");
        
        return  (List<Place>) query.getResultList();
           
    }catch (Exception e){
        System.out.println("Errore nella query getInvitedUsers");
    }
         return null;
    }
}
