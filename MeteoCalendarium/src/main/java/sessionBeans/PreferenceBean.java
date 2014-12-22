/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import HelpClasses.PreferenceHelp;
import entities.Event;
import entities.MainCondition;
import entities.Preference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.EventManager;
import managerBeans.PreferenceManager;

/**
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class PreferenceBean {
    

     
    @EJB
    private PreferenceManager pm;
    
    PreferenceHelp preferenceHelp ;

    public PreferenceHelp getPreferenceHelp() {
        if(preferenceHelp ==null )
        {
            preferenceHelp=new PreferenceHelp();
        }
        return preferenceHelp;
    }

    public void setPreferenceHelp(PreferenceHelp preferenceHelp) {
        this.preferenceHelp = preferenceHelp;
    }
    
    public void save(PreferenceHelp help)
    {
        preferenceHelp=help; 
        for(int i=0;i<preferenceHelp.getSelectedPreference().size();i++)
        {
            pm.addPreference(new Preference(preferenceHelp,i));
        }
    }
    
    
    public List<String> listPref ()
    {
        return MainCondition.getListPref();
    }
    
    
}
