/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entity.Users;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import control.UserManagerInterface;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Bean That Manage Registrations
 *
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
    private Users user = new Users();

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * Save new Users
     *
     * @return
     */
    public String register() {
        try {
            um.save(user);
            return "registrationAuthentication.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Username Already USED"));
            return "";
        }

    }
    /*
     * ******************************************************************
     * GETTERS AND SETTER
     *******************************************************************
     */

    public Users getUser() {
        if (user == null) {
            user = new Users();
        }
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}
