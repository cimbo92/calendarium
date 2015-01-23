/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.BadWeatherNotificationManager;
import control.EventManager;
import control.ForecastManager;
import control.UserManager;
import entity.Event;
import entity.Forecast;
import entity.IDEvent;
import entity.MainCondition;
import entity.Place;
import entity.Users;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alessandro
 */
public class ScheduleViewTest {

    Event eventCreated = new Event();
    Event notCreatedAccepted = new Event();
    Event notCreatedNotAccepted = new Event();

    Users loggedUser = new Users();
    ScheduleView s = new ScheduleView();
    List<Event> listToSee = new ArrayList<>();

    private void initEvent() {
        loggedUser.setEmail("ale@a.it");

        eventCreated.setIdEvent(new IDEvent("2"));
        eventCreated.setPlace(new Place());
        Timestamp tmp = new Timestamp(2015, 1, 1, 1, 1, 1, 1);
        eventCreated.setCreator(loggedUser);
        eventCreated.setDescription("Test");
        eventCreated.setEndDate(tmp);
        eventCreated.setOutdoor(false);
        eventCreated.setPublicEvent(true);
        eventCreated.setStartDate(tmp);
        eventCreated.setTitle("title");
        notCreatedAccepted.setTitle("title2");
        notCreatedAccepted.setIdEvent(new IDEvent("3"));
        notCreatedAccepted.setCreator(new Users());
        listToSee.add(eventCreated);
        listToSee.add(notCreatedAccepted);
    }

    private void setQueries() {
        UserManager um = mock(UserManager.class);
        EventManager em = mock(EventManager.class);
        ForecastManager fm = mock(ForecastManager.class);
        BadWeatherNotificationManager bm = mock(BadWeatherNotificationManager.class);

        Forecast forecast = new Forecast();
        forecast.setMainCondition(new MainCondition());
        forecast.getMainCondition().setCondition("Rain");
        List<Forecast> listForecast = new ArrayList<>();
        s.em = em;
        s.um = um;
        s.fm = fm;
        s.bwm = bm;
        s.init();

        when(um.getLoggedUser()).thenReturn(loggedUser);
        when(em.loadCalendar(um.getLoggedUser())).thenReturn(listToSee);
        when(fm.getForecastOfEvent(eventCreated)).thenReturn(listForecast);
        when(fm.getForecastOfEvent(notCreatedAccepted)).thenReturn(listForecast);
        when(bm.isWarned(eventCreated)).thenReturn(Boolean.TRUE);
        when(bm.isWarned(eventCreated)).thenReturn(Boolean.FALSE);
    }

    private void initTest() {
        this.initEvent();
        this.setQueries();
    }

    @Test
    public void loadCalendar() {
        this.initTest();
        s.loadCalendar();

        assertTrue(s.getEventModel().getEvents().get(0).getTitle().equals(eventCreated.getTitle()));
        assertTrue(s.getEventModel().getEvents().get(1).getTitle().equals(notCreatedAccepted.getTitle()));
        assertTrue(!s.getEventModel().getEvents().get(0).getTitle().equals(notCreatedNotAccepted.getTitle()));

    }

}
