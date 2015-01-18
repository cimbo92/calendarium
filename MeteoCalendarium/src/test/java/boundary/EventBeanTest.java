/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package boundary;

import HelpClasses.EventCreation;
import control.EventManager;
import control.EventManagerInterface;
import control.IDEventManager;
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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author home
 */
public class EventBeanTest {
    
    private User u = new User();
    private Timestamp sd = new Timestamp(new java.util.Date().getTime()+100*(24*60*60*1000));
    private Timestamp ed = new Timestamp(new java.util.Date().getTime()+101*(24*60*60*1000));
    
    private EventManager em;
    private EntityManager entityManager;
    
    private String t = "Test";
    private Place p = new Place();
    private Event e = new Event();
    private EventCreation beanEvent = new EventCreation();
    private EventBean eb = new EventBean();
    
    @Test
    public void createEventTest(){
        
        List<Event> list = new ArrayList<Event>();
        
        em = new EventManager();
        entityManager = mock(EntityManager.class);
        IDEventManagerInterface idm = mock(IDEventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        EventManagerInterface eM = mock(EventManagerInterface.class);
        PreferenceManagerInterface pm = mock(PreferenceManagerInterface.class);
        UserEventManagerInterface uem = mock(UserEventManagerInterface.class);
        MailSenderManagerInterface mailSender = mock(MailSenderManagerInterface.class);
        FacesContext fc = mock(FacesContext.class);
        
        
        
        eb.setEm(eM);
        eb.setPm(pm);
        eb.setUem(uem);
        eb.setMailSender(mailSender);
        eb.setContext(fc);
        
        when(um.getLoggedUser()).thenReturn(u);
        eb.setUm(um);
        
        when(idm.findFirstFreeID()).thenReturn(new Long(-1));
        eb.setIdm(idm);
        
        Query query = mock(Query.class);
        when(entityManager.createNamedQuery("Select e From Event e Where e.creator.email= :mail")).thenReturn(query);
        when(query.setParameter(Matchers.anyString(), Matchers.anyObject())).thenReturn(query);
        when(query.getResultList()).thenReturn(list);
        em.setEm(entityManager);
        
        
                
        e = init(e);
        eb = init(eb);
        beanEvent = initB(beanEvent);
        eb.setBeanEvent(beanEvent);
        
        assertNotNull(em);
        
        list = em.getEventsCreated(u);
        
        eb.create();    
        list.add(e);
        
        list = em.getEventsCreated(u);
        
        
        assertEquals(e,list.get(0));
        
    }
    
    private Event init(Event e){
        
        u.setEmail("test@test.test");
        u.setGroupName("USER");
        u.setPassword("test");
        u.setPublicCalendar(true);
        
        p.setCity("Test");
        
        e.setCreator(u);
        e.setDescription("Test");
        e.setEndDate(ed);
        e.setIdEvent(new IDEvent("-1"));
        e.setOutdoor(false);
        e.setPlace(p);
        e.setPublicEvent(true);
        e.setStartDate(sd);
        e.setTitle(t);
        
        
        return e;
    }
    
    private EventBean init(EventBean eb) {
        
        eb.setEvent(e);
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
