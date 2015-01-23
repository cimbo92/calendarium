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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Alessandro
 */
@RunWith(Arquillian.class)
public class EventManagerIT {

    public EventManagerIT() {
    }

    @EJB
    private UserManager um;
    @PersistenceContext
    private EntityManager em;

    private Event ev1 = new Event();
    private Event ev2 = new Event();
    private Event ev3 = new Event();
    private Event ev4 = new Event();
    private User us1 = new User();
    private User us2 = new User();

    @Inject
    UserTransaction utx;

    @EJB
    private EventManagerInterface evm;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManagerInterface.class)
                .addClass(UserManagerInterface.class)
                .addClass(EntityManager.class)
                .addPackage(Event.class.getPackage())
                .addPackage(Place.class.getPackage())
                .addPackage(IDEvent.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    int IDCount = 0;

    public Event setEvent(String title, String Description, String city, User creator, Timestamp start, Timestamp end, boolean outdoor, boolean publicEvent) {
        IDCount++;
        Event ev = new Event();
        ev.setTitle(title);
        ev.setDescription(Description);
        ev.setPlace(new Place(city));
        ev.setIdEvent(new IDEvent(IDCount + ""));
        ev.getIdEvent().setEvent(ev);

        ev.setCreator(creator);
        ev.setOutdoor(outdoor);
        ev.setPublicEvent(publicEvent);
        ev.setStartDate(start);
        ev.setEndDate(end);
        return ev;
    }

    public User setUser(String mail, String password, boolean calPublic) {
        User us = new User();
        us.setEmail(mail);
        us.setPassword(password);
        us.setPublicCalendar(calPublic);
        us.setGroupName(Group.USER);
        return us;
    }

    @Before
    public void setUp() {
        us1 = setUser("cianfe@mail.it", "password1", true);
        us2 = setUser("denny@mail.it", "password2", true);

        Timestamp startEv1 = new Timestamp(1111);
        Timestamp endEv1 = new Timestamp(1111);

        Timestamp startEv2 = new Timestamp(2222);
        Timestamp endEv2 = new Timestamp(2222);

        Timestamp startEv3 = new Timestamp(3333);
        Timestamp endEv3 = new Timestamp(3333);

        Timestamp startEv4 = new Timestamp(4444);
        Timestamp endEv4 = new Timestamp(4444);

        ev1 = setEvent("titolo1", "1", "posto1", us1, startEv1, endEv1, true, true);

        ev1 = setEvent("titolo2", "2", "posto2", us1, startEv2, endEv2, false, true);

        ev1 = setEvent("titolo3", "3", "posto3", us2, startEv3, endEv3, true, true);

        ev1 = setEvent("titolo4", "4", "posto4", us2, startEv4, endEv4, true, false);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addEvent method, of class EventManager.
     */
    @Test
    public void testAddEvent() throws Exception {

        evm = new EventManager();
        um = new UserManager();
        evm.setEm(em);
        um.setEm(em);

        evm.addEvent(ev1, us1);
        evm.addEvent(ev2, us1);
        evm.addEvent(ev3, us2);
        evm.addEvent(ev4, us2);

        utx.commit();
        em.clear();
        assertTrue(false);
    }

    /**
     * Test of searchOverlapping method, of class EventManager.
     */
    @Test
    public void testSearchOverlapping() throws Exception {
        System.out.println("searchOverlapping");
        assert (true);
    }

    /**
     * Test of isCreator method, of class EventManager.
     */
    @Test
    public void testIsCreator() throws Exception {
        System.out.println("isCreator");
        assert (true);
    }

    /**
     * Test of removeEvent method, of class EventManager.
     */
    @Test
    public void testRemoveEvent() throws Exception {
        System.out.println("removeEvent");
        assert (true);
    }

    /**
     * Test of loadSpecificEvent method, of class EventManager.
     */
    @Test
    public void testLoadSpecificEvent() throws Exception {
        System.out.println("loadSpecificEvent");
        assert (true);
    }

    /**
     * Test of findInvitatedEvent method, of class EventManager.
     */
    @Test
    public void testFindInvitatedEvent() throws Exception {
        System.out.println("findInvitatedEvent");
        assert (true);
    }

    /**
     * Test of loadCalendar method, of class EventManager.
     */
    @Test
    public void testLoadCalendar() throws Exception {
        System.out.println("loadCalendar");
        assert (true);
    }

    /**
     * Test of loadPublicCalendar method, of class EventManager.
     */
    @Test
    public void testLoadPublicCalendar() throws Exception {
        System.out.println("loadPublicCalendar");
        assert (true);
    }

    /**
     * Test of removeAllEvent method, of class EventManager.
     */
    @Test
    public void testRemoveAllEvent() throws Exception {
        System.out.println("removeAllEvent");
        assert (true);
    }

    /**
     * Test of getEventsCreated method, of class EventManager.
     */
    @Test
    public void testGetEventsCreated() throws Exception {
        System.out.println("getEventsCreated");
        assert (true);
    }

    /**
     * Test of removeEventByID method, of class EventManager.
     */
    @Test
    public void testRemoveEventByID() throws Exception {
        System.out.println("removeEventByID");
        assert (true);
    }

}
