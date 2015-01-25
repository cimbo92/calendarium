/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.control;

import business.security.entity.Place;
import business.security.forecast.WeatherForecastResponse;
import business.security.forecast.WeatherStatusResponse;
import java.io.IOException;
import javax.ejb.Remote;
import org.json.JSONException;

/**
 *
 * @author home
 */
@Remote
public interface OwmClientInterface {

    public WeatherForecastResponse forecastWeatherAtCity(String cityName) throws JSONException, IOException;

    public WeatherStatusResponse currentWeatherAtCity(String cityName) throws IOException, JSONException;

    public WeatherForecastResponse tenForecastWeatherAtCity(String cityName) throws JSONException, IOException;
    
    public void checkWeatherRecent();
    
    public void push(Place p);
}
