/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.User;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import managerBeans.UserManagerInterface;

/**
 *Bean That Manage Registrations
 * @author Alessandro De Angelis
 */
@Named
@RequestScoped
public class RegistrationBean {

/*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    private UserManagerInterface um;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    private User user = new User();

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */

    /**
    * Save new User
    */
    public String register() {
        um.save(user);
        return "registrationAuthentication.xhtml?faces-redirect=true";
    }
    /*
     * ******************************************************************
     * GETTERS AND SETTER
     *******************************************************************
     */


    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
