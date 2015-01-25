/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.boundary;

import business.security.entity.Group;
import business.security.entity.Users;
import java.security.Principal;
import java.util.List;
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
public class UserManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    /**
     *
     * @return a list with all registered users
     */

    public List<String> getListUsers() {
        Query query;
        query = em.createQuery("SELECT e.email FROM Users e WHERE e.email!= :usermail").setParameter("usermail", principal.getName());
        List<String> user;
        user = query.getResultList();
        return user;
    }

    /**
     * this function search in the database if there is a user with the email
     * passed from parameters
     *
     * @param mail
     * @return the user
     */

    public Users findByMail(String mail) {

        Query query;
        query = em.createQuery("SELECT u FROM Users u WHERE u.email= :mail ").setParameter("mail", mail);

        List<Users> user = query.getResultList();
        if (!user.isEmpty()) {
            return user.get(0);
        }
        return null;

    }

    /**
     * This function sets the calendar of the user passed from parameters as the
     * value status
     *
     * @param status
     * @param user
     */

    public void setCalendar(boolean status, Users user) {

        try {

            Query query = em.createQuery("UPDATE Users  u SET u.publicCalendar= :status Where u.email= :user");
            query.setParameter("status", status);
            query.setParameter("user", user.getEmail());
            query.executeUpdate();

        } catch (Exception e) {
            System.out.println("Errore nella query setCalendar");
        }
    }

    /**
     *
     * @return a list of emails that contains all users with calendar sets as
     * public
     */

    public List<String> getListUsersPublic() {

        Query query;
        query = em.createQuery("SELECT e.email FROM Users e WHERE e.email!= :usermail and e.publicCalendar=1").setParameter("usermail", principal.getName());
        List<String> user;
        user = query.getResultList();
        return user;
    }


    public EntityManager getEm() {
        return em;
    }


    public void setEm(EntityManager em) {
        this.em = em;
    }


    public void save(Users user) throws Exception {

        user.setGroupName(Group.USERS);
        em.persist(user);

    }


    public void unregister() {
        em.remove(getLoggedUser());
    }


    public Users getLoggedUser() {
        return em.find(Users.class, principal.getName());

    }
}
