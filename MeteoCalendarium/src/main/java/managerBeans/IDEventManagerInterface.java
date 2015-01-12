/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.iDEvent;
import javax.ejb.Remote;

/**
 *
 * @author Alessandro
 */
@Remote
public interface IDEventManagerInterface {
    public Long findFirstFreeID();

   
}
