/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.EventManagerInterface;
import control.IDEventManagerInterface;
import control.MailSenderManagerInterface;
import control.PreferenceManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;
import entity.Event;
import entity.IDEvent;
import entity.Place;
import entity.User;
import java.sql.Timestamp;
import javax.faces.context.FacesContext;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Alessandro
 */
public class EventDetailsTest {

    private EventBean eb = new EventBean();
    private User u = new User();
    private Event e = new Event();

    private void init() {
        eb.resetBean();
        this.initUser();
        this.initEvent();
    }

    private void initUser() {
        u.setEmail("test@test.test");
        u.setGroupName("USER");
        u.setPassword("test");
        u.setPublicCalendar(true);

    }

    private void initEvent() {
        e = new Event();
        e.setIdEvent(new IDEvent("1"));
        e.setPlace(new Place());
        Timestamp tmp = new Timestamp(2015, 1, 1, 1, 1, 1, 1);
        e.setCreator(u);
       Place p = new Place();
        p.setCity("posto");
        e.setPlace(p);
        e.setDescription("Test");
        e.setEndDate(tmp);
        e.setOutdoor(false);
        e.setPublicEvent(true);
        e.setStartDate(tmp);
        e.setTitle("title");

    }

    private void initManagers() {
        IDEventManagerInterface idm = mock(IDEventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        EventManagerInterface em = mock(EventManagerInterface.class);
        PreferenceManagerInterface pm = mock(PreferenceManagerInterface.class);
        UserEventManagerInterface uem = mock(UserEventManagerInterface.class);
        MailSenderManagerInterface mailSender = mock(MailSenderManagerInterface.class);
        FacesContext fc = mock(FacesContext.class);
        eb.setEm(em);
        eb.setPm(pm);
        eb.setUem(uem);
        eb.setMailSender(mailSender);
        eb.setContext(fc);
        eb.setUm(um);
    }

    @Test
    public void SelectEvent() {
//
        init();
        initManagers();
//
        DefaultScheduleEvent defEvent = new DefaultScheduleEvent();
        defEvent.setStartDate(e.getStartDate());
        defEvent.setEndDate(e.getEndDate());
        defEvent.setDescription(e.getIdEvent().getId() + "");

        //
        when(eb.getUm().getLoggedUser()).thenReturn(u);
        when(eb.getEm().loadSpecificEvent(defEvent.getDescription())).thenReturn(e);
        when(eb.getEm().isCreator(e, u)).thenReturn(Boolean.TRUE);
        //
        eb.loadFromEventSelect(defEvent);
        //
        assertTrue(eb.getBeanEvent().getTitle().equals("title"));
    }

}
