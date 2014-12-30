/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Group;
import entities.User;
import java.security.Principal;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author home
 */
@Stateless
public class UserManager implements UserManagerInterface {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;

    @Override
    public void save(User user) {
        user.setGroupName(Group.USER);
        em.persist(user);
    }

    @Override
    public void unregister() {
        em.remove(getLoggedUser());
    }

    @Override
    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    @Override
    public List<String> getListUsers()
    {
        //query utenti
        
        Query query;
        query = em.createQuery("SELECT e.email " + "FROM User e");
        
        List<String> user;
        user = query.getResultList();
        System.out.println(user.get(0));
        return user;
    }

    @Override
    public User getUser(String idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User findByMail(String mail) {
        
        Query query;
        query =em.createQuery( "SELECT u FROM User u WHERE u.email= :mail " ).setParameter("mail", mail);
        
        List<User> user =query.getResultList(); 
       return user.get(0);
        
    }
}
