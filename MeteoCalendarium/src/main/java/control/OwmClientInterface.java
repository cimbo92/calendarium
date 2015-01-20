/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Place;
import forecastService.WeatherForecastResponse;
import forecastService.WeatherStatusResponse;
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
