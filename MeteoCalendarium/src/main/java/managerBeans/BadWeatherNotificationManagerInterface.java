/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.BadWeatherNotification;
import entities.User;
import java.util.List;

/**
 *
 * @author home
 */
public interface BadWeatherNotificationManagerInterface {
    
     public List<BadWeatherNotification> searchByDate(String date);
 
     public List<BadWeatherNotification> getAllUserBadWeatherNotification(User creator);
      
     
}
