/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.MainCondition;
import java.security.Principal;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Alessandro
 */
@Stateless
public class MainConditionManager {
     @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    

    public void updateMainCondition(MainCondition mainCondition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addMainCondition(MainCondition mainCondition) {
        
        em.persist(mainCondition);
    }
}
