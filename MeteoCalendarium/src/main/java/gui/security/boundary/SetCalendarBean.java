/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.security.boundary;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import business.security.boundary.UserManager;

/**
 * Manager of Public Calendar Settings
 *
 * @author Alessandro De Angelis
 */
@Named
@RequestScoped
public class SetCalendarBean {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    UserManager um;

    /*
     * ******************************************************************
     * FIELDS
     *******************************************************************
     */
    private boolean status = true;

    /*
     * ******************************************************************
     * PUBLIC FUNCTIONS
     *******************************************************************
     */
    /**
     * save decision of user in database
     */
    public void saveDecision() {
        um.setCalendar(status, um.getLoggedUser());
    }

    /*
     * ******************************************************************
     * GETTERS AND SETTERS
     *******************************************************************
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return um.getLoggedUser().isPublicCalendar();
    }
}
