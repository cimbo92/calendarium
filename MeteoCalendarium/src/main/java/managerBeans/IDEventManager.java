/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import java.security.Principal;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Convert;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alessandro
 */
@Stateless
@Remote(IDEventManagerInterface.class)
public class IDEventManager implements IDEventManagerInterface {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    
    @Override
    public Long findMax(){

        Query query;
        query =em.createQuery( "SELECT MAX(i.id) FROM iDEvent i" );
        
        List<Long> id =query.getResultList(); 
        if(id.get(0)==null)
        {
            return Long.parseLong("1");
        }
        else
        {
        return id.get(0)+1;
        }
        
        
    }
    
    
}
