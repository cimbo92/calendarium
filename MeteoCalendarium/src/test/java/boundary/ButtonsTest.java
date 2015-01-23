/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.EventCreation;
import control.BadWeatherNotificationManager;
import control.EventManager;
import control.IDEventManager;
import control.MailSenderManager;
import control.PreferenceManager;
import control.UserEventManager;
import control.UserManager;
import entity.Event;
import entity.IDEvent;
import entity.Place;
import entity.Users;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author Alessandro
 */
public class ButtonsTest {

    private EventBean eb = new EventBean();
    private Users u = new Users();
    private Event e = new Event();

    private void init() {
        eb.resetBean();
        this.initUser();
        this.initEvent();
    }

    private void initUser() {
        u.setEmail("test@test.test");
        u.setGroupName("USERS");
        u.setPassword("test");
        u.setPublicCalendar(true);

    }

    private void initEvent() {
        e = new Event();
        e.setIdEvent(new IDEvent("1"));
        Place p = new Place();
        p.setCity("posto");
        e.setPlace(p);
        Timestamp tmp = new Timestamp(2015, 1, 1, 1, 1, 1, 1);
        e.setCreator(u);
        e.setDescription("Test");
        e.setEndDate(tmp);
        e.setOutdoor(false);
        e.setPublicEvent(true);
        e.setStartDate(tmp);
        e.setTitle("title");

    }

    private void initManagers() {
        IDEventManager idm = mock(IDEventManager.class);
        UserManager um = mock(UserManager.class);
        EventManager em = mock(EventManager.class);
        PreferenceManager pm = mock(PreferenceManager.class);
        UserEventManager uem = mock(UserEventManager.class);
        MailSenderManager mailSender = mock(MailSenderManager.class);
        FacesContext fc = mock(FacesContext.class);
        eb.setEm(em);
        eb.setPm(pm);
        eb.setUem(uem);
        eb.setMailSender(mailSender);
        eb.setContext(fc);
        eb.setUm(um);
    }

    @Test
    public void ModifcationButtonOwnEvent() {
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
        assertTrue(eb.isIsOwnEvent());
        assertTrue(!eb.isCanDecline());
        assertTrue(eb.canEliminate());
        assertTrue(!eb.isCreating());

    }

    @Test
    public void ModifcationButtonNotOwnEvent() {
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
        when(eb.getEm().isCreator(e, u)).thenReturn(Boolean.FALSE);
        //
        eb.loadFromEventSelect(defEvent);
        //
        assertTrue(!eb.isIsOwnEvent());
        assertTrue(eb.isCanDecline());
        assertTrue(!eb.canEliminate());
        assertTrue(!eb.isCreating());

    }

    @Test
    public void CreationButton() {
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
        eb.loadFromDateSelect(defEvent);
        //
        assertTrue(eb.isIsOwnEvent());
        assertTrue(!eb.isCanDecline());
        assertTrue(!eb.canEliminate());
        assertTrue(eb.isCreating());
    }

    @Test
    public void InvitationButtonDisabled() {
        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        InvitationBean ib = new InvitationBean();

        List<Event> list2 = new ArrayList<>();

        EventCreation e1 = new EventCreation();
        e1.setTitle("No Invitation");

        when(um.getLoggedUser()).thenReturn(u);

        when(em.findInvitatedEvent(um.getLoggedUser())).thenReturn(list2);

        ib.um = um;
        ib.em = em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        assertTrue(!ib.isEnableInvitation());
    }

    @Test
    public void WarningsButtonDisabled() {

        WarningBean wb = new WarningBean();

        EventManager em = mock(EventManager.class);
        UserManager um = mock(UserManager.class);
        BadWeatherNotificationManager bwnm = mock(BadWeatherNotificationManager.class);

        wb.em = em;
        wb.um = um;
        wb.bwnm = bwnm;
        List<Event> listEvent = new ArrayList<>();
        when(bwnm.findWarnings((Users) (Matchers.anyObject()))).thenReturn(listEvent);
        wb.setWarnings(new ArrayList<Event>());
        wb.loadWarnings();

        assertTrue(!wb.isEnableModify());

    }
}
