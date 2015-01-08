/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.BadWeatherNotificationManagerInterface;
import managerBeans.UserManagerInterface;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class WarningBean {
    
    @EJB
    private BadWeatherNotificationManagerInterface bwnm;
    @EJB
    UserManagerInterface um;
    
    private List<Event> warnings;
    private List<Timestamp> solutions;

    
     @PostConstruct
    public void init() {
        warnings = new ArrayList<>();
        warnings = bwnm.findWarnings(um.getLoggedUser());
        if(!warnings.isEmpty())
        {
            searchSolution();
        }
        
    }
    
    public void searchSolution()
    {
        
        solutions = bwnm.findSolution(warnings);
        if(!solutions.isEmpty())
        System.out.println("Va: " + solutions.get(0).getDate());
        
    }
    
    public List<Event> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Event> warnings) {
        this.warnings = warnings;
    }
    
    
    
}
