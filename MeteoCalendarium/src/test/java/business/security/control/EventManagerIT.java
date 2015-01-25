/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business.security.control;

import business.security.boundary.DestroyerManager;
import business.security.boundary.UserEventManager;
import business.security.boundary.UserManager;
import business.security.boundary.EventManager;
import business.security.entity.Event;
import business.security.entity.Group;
import business.security.entity.IDEvent;
import business.security.entity.Place;
import business.security.entity.UserEvent;
import business.security.entity.Users;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Alessandro
 */
@RunWith(Arquillian.class)
public class EventManagerIT {

    @EJB
    private UserManager um;
    @EJB
    private EventManager emi;
    @EJB
    private UserEventManager uem;
    @EJB
    private DestroyerManager dm;

    @PersistenceContext
    private EntityManager em;

    Users user1, user2;
    Event event1, event2;
    UserEvent userEvent1, userEvent2;

    int IDCount;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addClass(UserEventManager.class)
                .addClass(EventManager.class)
                .addClass(EntityManager.class)
                .addClass(DestroyerManager.class)
                .addPackage(Users.class.getPackage())
                .addPackage(Event.class.getPackage())
                .addPackage(UserEvent.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Before
    public void prepareTests() throws Exception {
        clearData();
        insertData();
        IDCount = 0;
    }

    @Test
    public void testAddEvent() {

        Event ev = emi.loadSpecificEvent(event1.getIdEvent().getId() + "");
        assertEquals(ev.getIdEvent().getId(), event1.getIdEvent().getId());
    }

    @Test
    public void testGetInvitatedUsers() {
        List<Event> list = emi.findInvitatedEvent(user2);
        assertEquals(list.get(0).getTitle(), event1.getTitle());
    }

    @Test
    public void test() {
        List<Event> list = emi.getEventsCreated(user1);
        assertEquals(list.get(0).getTitle(), event1.getTitle());
    }

    @Test
    public void testLoadPublicCalendar() {
        assertEquals((int) 1, emi.loadPublicCalendar("user1@mail.it").size());
        assertEquals(emi.loadPublicCalendar("user1@mail.it").get(0).getTitle(), event1.getTitle());
    }

    @Test
    public void testLoadCalendar() {
        List<Event> list = emi.getEventsCreated(user1);
        assertEquals(list.size(), (int) 2);
        assertEquals(list.get(0).getTitle(), event1.getTitle());
        assertEquals(list.get(1).getTitle(), event2.getTitle());
    }

    public Event setEvent(String title, String Description, String city, Users creator, Timestamp start, Timestamp end, boolean outdoor, boolean publicEvent) {
        IDCount++;
        Event ev = new Event();
        ev.setTitle(title);
        ev.setDescription(Description);
        ev.setPlace(new Place(city));
        ev.setIdEvent(new IDEvent(IDCount + ""));
        ev.setCreator(creator);
        ev.setOutdoor(outdoor);
        ev.setPublicEvent(publicEvent);
        ev.setStartDate(start);
        ev.setEndDate(end);
        return ev;
    }

    private void insertData() throws Exception {
        user1 = new Users();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USERS);
        user1.setPublicCalendar(false);
        um.save(user1);
        user2 = new Users();
        user2.setEmail("user2@mail.it");
        user2.setPassword("user2");
        user2.setGroupName(Group.USERS);
        user2.setPublicCalendar(false);
        um.save(user2);
        event1 = setEvent("Title", "Description", "Milan", user1, new Timestamp(1111), new Timestamp(11122), true, true);
        emi.addEvent(event1, user1);
        event2 = setEvent("Title2", "Description", "Milan", user1, new Timestamp(111122), new Timestamp(112222), true, false);
        emi.addEvent(event2, user1);

        userEvent1 = new UserEvent(event1, user1, true);
        uem.addUserEvent(userEvent1);
        userEvent2 = new UserEvent(event1, user2, false);
        uem.addUserEvent(userEvent2);

    }

    private void clearData() throws Exception {
        System.out.println("Dumping old records...");
        dm.deleteAllData();
    }

}
