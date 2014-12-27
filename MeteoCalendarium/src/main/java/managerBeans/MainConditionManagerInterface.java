/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.MainCondition;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
interface MainConditionManagerInterface {
    
    public void updateMainCondition(MainCondition mainCondition);
    
    public void addMainCondition(MainCondition mainCondition);
    
    
}
