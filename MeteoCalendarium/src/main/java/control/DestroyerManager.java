/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.security.Principal;
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
public class DestroyerManager {

     @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;


    public void deleteAllData(){
          Query query1 = em.createQuery("Delete From Preference p");
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue");
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e ");
        query3.executeUpdate();
        Query query4 = em.createQuery("Delete From IDEvent id");
        query4.executeUpdate();
        Query query5 = em.createQuery("Delete From Users u");
        query5.executeUpdate();
        Query query6 = em.createQuery("Delete From MainCondition m");
        query6.executeUpdate();
        Query query7 = em.createQuery("Delete From Place p");
        query7.executeUpdate();
            Query query9 = em.createQuery("Delete From Forecast f");
        query9.executeUpdate();
    }
}
