/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Preference;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
public interface PreferenceManagerInterface {

    
    public void addPreference(Preference get);
    
    public List<String> getPreferenceOfEvent(Event event);
           
}
