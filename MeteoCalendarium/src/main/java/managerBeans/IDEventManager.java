/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import entities.iDEvent;
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
    public Long findFirstFreeID(){

        Query query;
        query =em.createQuery( "SELECT id.id From iDEvent id order by id.id" );
        
        List<Long> id =query.getResultList(); 
       
        if(id.size()==0)
        {
            return Long.parseLong("1");
        }
        
        for(int i=0;i<id.size()-1;i++)
        {
            if(id.get(i+1)!=id.get(i)+1)
            {
                return id.get(i)+1;
            }
        }
        return id.get(id.size()-1)+1;        
    }
    
    
}
