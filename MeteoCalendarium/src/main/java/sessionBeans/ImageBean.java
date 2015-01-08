/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Alessandro
 */
@RequestScoped
@Named
public class ImageBean {
    
    public String getUserImage()
    {
        return "https://calendarium.googlecode.com/git/MeteoCalendarium/src/main/resources/images/demo.jpg";
    }
    
    
}
