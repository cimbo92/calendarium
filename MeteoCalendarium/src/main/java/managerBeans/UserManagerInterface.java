/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.User;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
public interface UserManagerInterface {
    
     public void save(User user);
     
     public void unregister();
     
     public User getLoggedUser();
     
     public User findByMail(String mail);
     
     public List<String> getListUsers();
     
     public void setCalendar(boolean status, User user);
             

}
