/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Group;
import entity.User;
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
public class UserManager implements UserManagerInterface {

    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    /**
     *
     * @return a list with all registered users
     */
    @Override
    public List<String> getListUsers() {
        Query query;
        query = em.createQuery("SELECT e.email FROM User e WHERE e.email!= :usermail").setParameter("usermail", principal.getName());
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
    @Override
    public User findByMail(String mail) {

        Query query;
        query = em.createQuery("SELECT u FROM User u WHERE u.email= :mail ").setParameter("mail", mail);

        List<User> user = query.getResultList();
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
    @Override
    public void setCalendar(boolean status, User user) {

        try {

            Query query = em.createQuery("UPDATE User SET publicCalendar= :status where email= :user");
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
    @Override
    public List<String> getListUsersPublic() {

        Query query;
        query = em.createQuery("SELECT e.email FROM User e WHERE e.email!= :usermail and e.publicCalendar=1").setParameter("usermail", principal.getName());
        List<String> user;
        user = query.getResultList();
        return user;
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(User user) throws Exception {

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
}
