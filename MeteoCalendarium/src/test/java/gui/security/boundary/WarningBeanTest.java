/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.security.boundary;

import gui.security.boundary.WarningBean;
import business.security.boundary.BadWeatherNotificationManager;
import business.security.boundary.EventManager;
import business.security.boundary.UserManager;
import business.security.entity.Event;
import business.security.entity.Place;
import business.security.entity.Users;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author alessandro
 */
public class WarningBeanTest {

    Query query = mock(Query.class);

    private Event initEvent(String title, String place) {
        Event e1 = new Event();
        e1.setTitle(title);
        e1.setPlace(new Place(place));
        return e1;
    }

    /**
     * Test of loadWarnings method, of class WarningBean.
     */
    @Test
    public void testLoadWarnings() {

        WarningBean wb = new WarningBean();

        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        BadWeatherNotificationManager bwnm = mock(BadWeatherNotificationManager.class);

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");

        List<Event> listEvent = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1", "Milan");
        Event eventtemp2 = this.initEvent("prova2", "Milan");

        listEvent.add(eventtemp1);
        listEvent.add(eventtemp2);

        when(um.getLoggedUser()).thenReturn(userTest);
        when(bwnm.findWarnings(um.getLoggedUser())).thenReturn(listEvent);

        wb.em = em;
        wb.um = um;
        wb.bwnm = bwnm;
        wb.setWarnings(new ArrayList<Event>());
        wb.loadWarnings();

        List<Event> temp = wb.getWarnings();

        assertTrue(temp.get(0).getTitle().equals(eventtemp1.getTitle()));

        listEvent = new ArrayList<>();
        when(bwnm.findWarnings(um.getLoggedUser())).thenReturn(listEvent);
        wb.setWarnings(new ArrayList<Event>());
        wb.loadWarnings();

        temp = wb.getWarnings();
        assertTrue(temp.get(0).getTitle().equals("No Warnings"));
        assertEquals(temp.get(0).getIdEvent().getId(), new Long(-1));

    }

    /**
     * Test of searchSolution method, of class WarningBean.
     */
    @Test
    public void testSearchSolution() {

        WarningBean wb = new WarningBean();

        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        BadWeatherNotificationManager bwnm = mock(BadWeatherNotificationManager.class);

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");

        List<Event> listEvent = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1", "Milan");
        Event eventtemp2 = this.initEvent("prova2", "Milan");

        List<Timestamp> listDate = new ArrayList<>();
        Timestamp temp1 = new Timestamp(1235);
        Timestamp temp2 = new Timestamp(1230000);

        listEvent.add(eventtemp1);
        listEvent.add(eventtemp2);

        listDate.add(temp1);
        listDate.add(temp2);

        when(um.getLoggedUser()).thenReturn(userTest);
        when(bwnm.findSolution(listEvent)).thenReturn(listDate);
        when(bwnm.findWarnings(um.getLoggedUser())).thenReturn(listEvent);

        wb.em = em;
        wb.um = um;
        wb.bwnm = bwnm;
        wb.setSolutions(new ArrayList<Timestamp>());
        wb.loadWarnings();

        List<Timestamp> sol = wb.getSolutions();

        assertEquals(sol.get(0).getTime(), temp1.getTime());

    }

}
