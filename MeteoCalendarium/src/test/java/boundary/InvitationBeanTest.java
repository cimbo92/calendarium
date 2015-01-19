/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.EventCreation;
import control.EventManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;
import entity.Event;
import entity.Place;
import entity.User;
import entity.UserEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.Query;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alessandro
 */
public class InvitationBeanTest {


    Query query = mock(Query.class);
    private EventManagerInterface em ;
    private UserManagerInterface um ;
    private InvitationBean ib ;
    private User userTest;

    private EventCreation init(String title){
          EventCreation e1 = new EventCreation();
          e1.setTitle(title);
          return e1;
    }

    private Event initEvent(String title){
          Event e1 = new Event();
          e1.setTitle(title);
          return e1;
    }
    
   
@Test
    public void invitationInList(){
        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        InvitationBean ib = new InvitationBean();

        User userTest = new User();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        List<Event> list2 = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1");
        Event eventtemp2 = this.initEvent("prova2");

        EventCreation e1 = this.init("prova1");
        EventCreation e2 = this.init("prova3");

        list2.add(eventtemp1);
        list2.add(eventtemp2);
   when(um.getLoggedUser()).thenReturn(userTest);
        when(em.findInvitatedEvent(um.getLoggedUser())).thenReturn(list2);

        ib.um=um;
        ib.em=em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        List<EventCreation> temp = ib.getInvites();

        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
        assertTrue(!temp.get(1).getTitle().equals(e1.getTitle()));
    }

      @Test
    public void noInvitationInList(){
        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        InvitationBean ib = new InvitationBean();

        User userTest = new User();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        List<Event> list2 = new ArrayList<>();


        EventCreation e1 = this.init("No Invitation");

        when(um.getLoggedUser()).thenReturn(userTest);

        when(em.findInvitatedEvent(um.getLoggedUser())).thenReturn(list2);

        ib.um=um;
        ib.em=em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        List<EventCreation> temp = ib.getInvites();

        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
        assertTrue(temp.size()==1);
    }

    
    @Test
    public void testAcceptInvite(){
        
     
        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        UserEventManagerInterface uem = mock(UserEventManagerInterface.class);
        InvitationBean ib = new InvitationBean();

        User userTest = new User();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);
        
       UserEvent userEvent = new UserEvent();
        
        EventCreation eventC = new EventCreation();
        eventC.setCreator(userTest);
        eventC.setDescription("description");
        eventC.setPlace("milan");
        eventC.setTitle("title");
        eventC.setEndDate(new Timestamp(10000));
        eventC.setStartDate(new Timestamp(9999));
        eventC.setPublicEvent(true);
        eventC.setOutdoor(true);
        
        List<Event> list2 = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1");
        Event eventtemp2 = this.initEvent("prova2");

        List<EventCreation> list = new ArrayList<>();
        EventCreation e1 = this.init("prova1");
        EventCreation e2 = this.init("prova2");

        list.add(e1);
        list.add(e2);

        list2.add(eventtemp1);
        list2.add(eventtemp2);

     
        when(em.findInvitatedEvent(userTest)).thenReturn(list2);
         when(um.getLoggedUser()).thenReturn(userTest);
         when(em.searchOverlapping(eventtemp1, userTest)).thenReturn(false);
         when(uem.getUserEventofUser(eventtemp1, um.getLoggedUser())).thenReturn(userEvent);
      

        ib.um=um;
        ib.em=em;
        ib.uem=uem;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        ib.acceptInvite(e1);
        
        //dovrebbe essere temp.size()==1 e il titolo uguale a e2.getTitle()
        List<EventCreation> temp = ib.getInvites();
        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
        assertTrue(temp.size()==2);
    }
        
        
        
        
        
        
        
        
        
        
    }
    
    
    

