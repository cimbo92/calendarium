/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.Forecast;
import entities.Place;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author home
 */
@Remote
public interface ForecastManagerInterface {
 
    public List<Forecast> getForecastInPlace(Place place);
    
}
