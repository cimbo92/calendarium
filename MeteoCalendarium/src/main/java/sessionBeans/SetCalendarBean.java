/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.UserManagerInterface;

/**
 *
 * @author home
 */
@Named
@RequestScoped
public class SetCalendarBean {
    
    @EJB
    private UserManagerInterface um;
   
    private boolean status=true;

    public boolean isStatus() {
        return um.getLoggedUser().isPublicCalendar();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public void saveDecision(){
        um.setCalendar(status, um.getLoggedUser());
    }
    
}