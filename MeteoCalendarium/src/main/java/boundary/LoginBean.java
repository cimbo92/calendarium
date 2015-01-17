/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import control.UserManagerInterface;

@RequestScoped
@Stateful
@Named("loginBean")
/**
 * Bean That Manage Invitations
 *
 * @author home
 */
public class LoginBean {

    /*
     * ******************************************************************
     * ;MANAGERS
     *******************************************************************
     */
    @EJB
    UserManagerInterface um;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    private String username;
    private String password;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * Return email of logged User
     *
     * @return
     */
    public String getName() {
        return um.getLoggedUser().getEmail();
    }

    /**
     * login request
     *
     * @return
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            return "";
        }
        return "calendar?faces-redirect=true";
    }

    /**
     * logout request
     *
     * @return
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
        return "registrationAuthentication?faces-redirect=true";
    }

    /*
     * ******************************************************************
     * GETERS AND SETTER
     *******************************************************************
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
