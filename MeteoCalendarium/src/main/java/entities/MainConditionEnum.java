/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author home
 * 
 * These main conditions are the only allowed into the system
 * 
 */

//TODO ricordiamo di fare uno switch in cui fltriamo le cose dell forecast esterno
//es parzialmente cloudy--> cloudy.
public enum MainConditionEnum {
    clearDay, clearNight, rain , snow ,sleet, wind, fog ,cloudy  
}
