/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Users;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author home
 */
public interface BadWeatherNotificationManagerInterface {

    public List<Event> findWarnings(Users creator);

    public List<Timestamp> findSolution(List<Event> eventWarning);

    public boolean isWarned(Event event);

}
