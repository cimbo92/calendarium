/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Event;
import entities.User;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 *
 * @author alessandro
 */

    @Singleton
@Remote(WarningManagerCheckerInterface.class)
public class WarningManagerChecker implements WarningManagerCheckerInterface {
    
    @EJB
    private UserManagerInterface um;
    @EJB
    private EventManagerInterface em;
    @EJB
    private BadWeatherNotificationManagerInterface bm;
    @EJB
    private MailSenderManagerInterface ms;
    @EJB
    private UserEventManagerInterface uem;
    
    
    private void threeDaysWarning(){
        
        Timestamp now = new Timestamp(new java.util.Date().getTime());
        
        long deltaThreeDay = 3*(24*60*60*1000);
        
        long nowLong = now.getTime();
        
        //Get all users
        List<User> users = uem.getUsersCreator();
        //Get all warnings
        Iterator<User> ite = users.iterator();
        while(ite.hasNext()){
            User u = ite.next();
            List<Event> eventWarning = bm.findWarnings(u);
            
            for(Event e : eventWarning){
                
                long d = e.getStartDate().getTime();
                
                if(d-nowLong >=0 && d-nowLong <= deltaThreeDay)
                    ms.sendMail(u.getEmail(),"Warning: "+e.getTitle(),"Message to notify a three days notification. For more info check on your personal page. \nStaff MeteoCalendarium");
            }
        }
        
        
        
        
    }
    
    private void oneDayWarning(){
        
        Timestamp now = new Timestamp(new java.util.Date().getTime());
        
        long deltaOneDay = (24*60*60*1000);
        
        long nowLong = now.getTime();
        
        List<User> users = uem.getUsersCreator();
        //Get all warnings
        Iterator<User> ite = users.iterator();
        while(ite.hasNext()){
            User u = ite.next();
            List<Event> eventWarning = bm.findWarnings(u);
            
            for(Event e : eventWarning){
                
                long d = e.getStartDate().getTime();
                
                if(d-nowLong >=0 && d-nowLong <= deltaOneDay){
                    List<User> invited = uem.getInvitedWhoAccepted(e);
                    for(User i : invited){
                        ms.sendMail(i.getEmail(),"Warning: "+e.getTitle(),"Message to notify a one days notification for an event in which you are invited. For more info check on your personal page. \nStaff MeteoCalendarium");
                    }
                }
            }
        }
    }
}