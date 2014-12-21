/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Event;
import entities.Preference;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class PreferenceBean {
    
    private Preference preference;
    
    public PreferenceBean() {};
    
    public Preference getPreference(){
        
        return preference;
    }
}
