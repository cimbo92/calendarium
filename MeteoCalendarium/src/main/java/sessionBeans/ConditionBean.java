/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.MainCondition;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import managerBeans.MainConditionManager;

/**
 *
 * @author Alessandro
 */
@Named
@RequestScoped
public class ConditionBean {
    
    @EJB
    private MainConditionManager mcm;

    
    
    public void MainCondition()
    {
        for(int i=0;i<MainCondition.getListPref().size();i++)
        {
            MainCondition main = new MainCondition();
            main.setCondition(MainCondition.getListPref().get(i));
            mcm.addMainCondition(main);
        }
    }
    
}
