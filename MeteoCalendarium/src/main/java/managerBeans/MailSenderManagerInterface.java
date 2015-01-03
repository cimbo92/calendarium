/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
public interface MailSenderManagerInterface {
    
    public void sendMail(String to, String subject, String body);
    
}
