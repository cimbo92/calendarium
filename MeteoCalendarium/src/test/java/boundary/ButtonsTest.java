/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.EventCreation;
import control.BadWeatherNotificationManagerInterface;
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
        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
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

        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        BadWeatherNotificationManagerInterface bwnm = mock(BadWeatherNotificationManagerInterface.class);

        wb.em = em;
        wb.um = um;
        wb.bwnm = bwnm;
        List<Event> listEvent = new ArrayList<>();
        when(bwnm.findWarnings((User) (Matchers.anyObject()))).thenReturn(listEvent);
        wb.setWarnings(new ArrayList<Event>());
        wb.loadWarnings();

        assertTrue(!wb.isEnableModify());

    }
   }


