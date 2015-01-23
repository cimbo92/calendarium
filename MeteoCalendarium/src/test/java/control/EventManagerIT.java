/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Group;
import entity.IDEvent;
import entity.Place;
import entity.User;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Alessandro
 */
public class EventManagerIT {

    public EventManagerIT() {
    }

    @EJB
    private UserManagerInterface um;
    @PersistenceContext
    private EntityManager em;


    private EventManager cut;

    @Deployment
    public static WebArchive createArchiveAndDeploy(){
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManagerInterface.class)
                .addClass(EntityManager.class)
                .addPackage(Event.class.getPackage())
                .addPackage(Place.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    int IDCount = 0;

    public Event setEvent(String title,String Description,String city,User creator,Timestamp start,Timestamp end,boolean outdoor,boolean publicEvent)
    {
        IDCount++;
        Event ev= new Event();
        ev.setTitle(title);
        ev.setDescription(Description);
        ev.setPlace(new Place(city));
        ev.setIdEvent(new IDEvent(IDCount+""));
        ev.setCreator(creator);
        ev.setOutdoor(outdoor);
        ev.setPublicEvent(publicEvent);
        ev.setStartDate(start);
        ev.setEndDate(end);
        return ev;
    }

    public User setUser(String mail ,String password, boolean calPublic)
    {
        User us = new User();
        us.setEmail(mail);
        us.setPassword(password);
        us.setPublicCalendar(calPublic);
        us.setGroupName(Group.USER);
        return us;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addEvent method, of class EventManager.
     */
    @Test
    public void testAddEvent() throws Exception {
      }

    /**
     * Test of searchOverlapping method, of class EventManager.
     */
    @Test
    public void testSearchOverlapping() throws Exception {
        System.out.println("searchOverlapping");

    }

    /**
     * Test of isCreator method, of class EventManager.
     */
    @Test
    public void testIsCreator() throws Exception {
        System.out.println("isCreator");

    }

    /**
     * Test of removeEvent method, of class EventManager.
     */
    @Test
    public void testRemoveEvent() throws Exception {
        System.out.println("removeEvent");

    }

    /**
     * Test of loadSpecificEvent method, of class EventManager.
     */
    @Test
    public void testLoadSpecificEvent() throws Exception {
        System.out.println("loadSpecificEvent");

    }

    /**
     * Test of findInvitatedEvent method, of class EventManager.
     */
    @Test
    public void testFindInvitatedEvent() throws Exception {
        System.out.println("findInvitatedEvent");

    }

    /**
     * Test of loadCalendar method, of class EventManager.
     */
    @Test
    public void testLoadCalendar() throws Exception {
        System.out.println("loadCalendar");

    }

    /**
     * Test of loadPublicCalendar method, of class EventManager.
     */
    @Test
    public void testLoadPublicCalendar() throws Exception {
        System.out.println("loadPublicCalendar");

    }

    /**
     * Test of removeAllEvent method, of class EventManager.
     */
    @Test
    public void testRemoveAllEvent() throws Exception {
        System.out.println("removeAllEvent");

    }

    /**
     * Test of getEventsCreated method, of class EventManager.
     */
    @Test
    public void testGetEventsCreated() throws Exception {
        System.out.println("getEventsCreated");

    }




    /**
     * Test of removeEventByID method, of class EventManager.
     */
    @Test
    public void testRemoveEventByID() throws Exception {
        System.out.println("removeEventByID");
     }

}
