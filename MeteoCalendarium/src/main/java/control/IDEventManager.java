/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.security.Principal;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * IDEvent Manager
 * @author Alessandro
 */
@Stateless
@Remote(IDEventManagerInterface.class)
public class IDEventManager implements IDEventManagerInterface {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;
    
    /**
     * return First free ID
     * @return
     */
    @Override
    public Long findFirstFreeID(){

        Query query;
        query =em.createQuery( "SELECT id.id From IDEvent id order by id.id" );

        List<Long> id =query.getResultList();

        if(id.isEmpty())
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
