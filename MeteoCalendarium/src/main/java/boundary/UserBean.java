/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.UserManager;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 * manager of logged user
 * @author Alessandro De Angelis
 */
@Named
@RequestScoped
public class UserBean {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    UserManager um;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * return logged user email
     *
     * @return
     */
    public String getName() {
        return um.getLoggedUser().getEmail();
    }
}
