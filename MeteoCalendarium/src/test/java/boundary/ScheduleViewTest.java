/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.BadWeatherNotificationManager;
import control.EventManagerInterface;
import control.ForecastManagerInterface;
import control.UserManagerInterface;
import entity.Event;
import entity.Forecast;
import entity.IDEvent;
import entity.MainCondition;
import entity.Place;
import entity.User;
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

        User loggedUser = new User();
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
        notCreatedAccepted.setCreator(new User());
        listToSee.add(eventCreated);
        listToSee.add(notCreatedAccepted);
    }

            private void setQueries(){
        UserManagerInterface um = mock(UserManagerInterface.class);
        EventManagerInterface em = mock(EventManagerInterface.class);
        ForecastManagerInterface fm = mock(ForecastManagerInterface.class);
        BadWeatherNotificationManager bm = mock(BadWeatherNotificationManager.class);

        Forecast forecast = new Forecast();
        forecast.setMainCondition(new MainCondition());
        forecast.getMainCondition().setCondition("Rain");
        List<Forecast> listForecast = new ArrayList<>();
        s.em=em;
        s.um=um;
        s.fm=fm;
        s.bwm=bm;
        s.init();

        when(um.getLoggedUser()).thenReturn(loggedUser);
        when(em.loadCalendar(um.getLoggedUser())).thenReturn(listToSee);
        when(fm.getForecastOfEvent(eventCreated)).thenReturn(listForecast);
        when(fm.getForecastOfEvent(notCreatedAccepted)).thenReturn(listForecast);
        when(bm.isWarned(eventCreated)).thenReturn(Boolean.TRUE);
        when(bm.isWarned(eventCreated)).thenReturn(Boolean.FALSE);
            }


    private void initTest(){
        this.initEvent();
        this.setQueries();
    }

        @Test
        public void loadCalendar(){
            this.initTest();
            s.loadCalendar();

            assertTrue(s.getEventModel().getEvents().get(0).getTitle().equals(eventCreated.getTitle()));
            assertTrue(s.getEventModel().getEvents().get(1).getTitle().equals(notCreatedAccepted.getTitle()));
            assertTrue(!s.getEventModel().getEvents().get(0).getTitle().equals(notCreatedNotAccepted.getTitle()));

        }



}
