/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.MainCondition;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import managerBeans.MainConditionManager;

/**
 *
 * @author Alessandro
 */
@Named
@RequestScoped
public class MainConditionBean {
    
    @EJB
    private MainConditionManager mcm;
    
    private MainCondition main=new MainCondition();
    
    public void addMainCondition()
    {
        for(int i=0;i<MainCondition.getListPref().size();i++)
        {
            main.setCondition(MainCondition.getListPref().get(i));
            mcm.addMainCondition(main);
        }
    }
    
}
