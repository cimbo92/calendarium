/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelpClasses;

import entities.Event;
import javax.ejb.EJB;
import managerBeans.BadWeatherNotificationManagerInterface;

/**
 *
 * @author Alessandro
 */
public class BannerLoader {
    @EJB BadWeatherNotificationManagerInterface bwm;
    
    
    public static String getBanner(Event event){
        String returnBanner = null;
        boolean isWarner=false;
        
        
        
        
        
        return returnBanner;
    }   
    
    
}
