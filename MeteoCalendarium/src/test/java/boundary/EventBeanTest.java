/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package boundary;

import HelpClasses.EventCreation;
import HelpClasses.OverlappingException;
import control.EventManager;
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
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.primefaces.context.RequestContext;

/**
 *
 * @author home
 */
public class EventBeanTest {
    
    private User u = new User();
    private Long now = new Long(String.valueOf(new java.util.Date().getTime()+100000000));
    private Long now2 = new Long(String.valueOf(now+111111111));
    private Timestamp sd = new Timestamp(now);
    private Timestamp ed = new Timestamp(now2);
    private EventManager em;
    private EntityManager entityManager;
    private String t = "Test";
    private Place p = new Place();
    private EventCreation beanEvent = new EventCreation();
    private EventBean eb = new EventBean();
    
    
    
    @Test
    public void deleteEventTest(){
        
        EventBean eb = new EventBean();
        eb.setBeanEvent(new EventCreation());
        
        List<Event> listEvent = new ArrayList<Event>();
        
        Event e1 = new Event();
        Event e2 = new Event();
        
        listEvent.add(e1);
        listEvent.add(e2);
        
        
        FacesContext fc = mock(FacesContext.class);
        RequestContext rc = mock(RequestContext.class);
        EventManagerInterface eM = mock(EventManagerInterface.class);
        
        
        eb.c = fc;
        eb.r = rc;
        eb.setEm(eM);
        
        eb.cancel();
               
    }
    
    @Test
    public void createEventTest() throws OverlappingException{
        
        EventBean eb = new EventBean();
        EventCreation beanEvent = new EventCreation();
        
        initB(beanEvent);
        
        List<Event> listEvent = new ArrayList<Event>();
        
        Event e1 = new Event();
        
        FacesContext fc = mock(FacesContext.class);
        RequestContext rc = mock(RequestContext.class);
        EventManagerInterface eM = mock(EventManagerInterface.class);
        PreferenceManagerInterface pm = mock(PreferenceManagerInterface.class);
        
        IDEventManagerInterface idm = mock(IDEventManagerInterface.class);
        when(idm.findFirstFreeID()).thenReturn(new Long(-1));
        
        UserManagerInterface um = mock(UserManagerInterface.class);
        when(um.getLoggedUser()).thenReturn(u);
        when(um.findByMail(t)).thenReturn(u);
        
        MailSenderManagerInterface mailSender = mock(MailSenderManagerInterface.class);
        
        UserEventManagerInterface uem = mock(UserEventManagerInterface.class);
        
        
        eb.setIdm(idm);
        eb.c = fc;
        eb.r = rc;
        eb.setBeanEvent(beanEvent);
        eb.setUm(um);
        eb.setStartDate(new Date(beanEvent.getStartDate().getTime()));
        eb.setEndDate(new Date(beanEvent.getEndDate().getTime()));
        eb.setEm(eM);
        eb.setPm(pm);
        eb.setMailSender(mailSender);
        eb.setUem(uem);
        
        //Creazione semplice
        eb.create();
        
        List<String> p = new ArrayList();
        p.add("Clear");
        List<String> u = new ArrayList();
        u.add("pippo");
        
          
        eb.setSelectedPreferences(p);
        eb.setSelectedUsers(u);
        
        //Creazione con preferenze ed invitati
        eb.create();
      
        
    }
    
    
    private EventBean init(EventBean eb) {
        
        
        eb.setEndDate(ed);
        eb.setStartDate(sd);
        eb.setIsOwnEvent(true);
        eb.setCreating(true);
        
        return eb;
    }
    
    private EventCreation initB(EventCreation beanEvent) {
        u.setEmail("test@test.test");
        u.setGroupName("USER");
        u.setPassword("test");
        u.setPublicCalendar(true);
        p.setCity("Test");
        beanEvent.setCreator(u);
        beanEvent.setDescription("Test");
        beanEvent.setEndDate(ed);
        beanEvent.setIdEvent(new IDEvent("-1"));
        beanEvent.setOutdoor(false);
        beanEvent.setPlace(p.getCity());
        beanEvent.setPublicEvent(true);
        beanEvent.setStartDate(sd);
        beanEvent.setTitle(t);
        
        
        return beanEvent;
    }
}
