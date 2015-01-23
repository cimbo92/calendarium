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
import entity.Users;
import entity.UserEvent;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author alessandro
 */
@RunWith(Arquillian.class)
public class UserEventManagerIT {

    public UserEventManagerIT() {
    }

    @EJB
    private UserManagerInterface um;
    @EJB
    private EventManagerInterface emi;
    @EJB
    private UserEventManagerInterface uem;
    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;
    Users user1;

    Users user2,user3;
    Event event1, event2;
    UserEvent userEvent1, userEvent2;

    int IDCount;


    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManagerInterface.class)
                .addClass(UserEventManagerInterface.class)
                .addClass(EventManagerInterface.class)
                .addClass(EntityManager.class)
                .addPackage(Users.class.getPackage())
                .addPackage(Event.class.getPackage())
                .addPackage(UserEvent.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Before
    public void prepareTests() throws Exception {
         emi = new EventManager();
        uem = new UserEventManager();
        um.setEm(em);
        emi.setEm(em);
        uem.setEm(em);
        clearData();
        insertData();
        IDCount=0;
    }

    /**
     * Test of findEventCreator method, of class UserEventManager.
     */
    @Test
    public void testFindEventCreator() throws Exception {

        assertEquals(user1, uem.findEventCreator(event1));
    }

    /**
     * Test of getUserEventofUser method, of class UserEventManager.
     */
    @Test
    public void testGetUserEventofUser() throws Exception {
       assertEquals(userEvent1, uem.getUserEventofUser(event1, user1));
    }


    /**
     * Test of invitedUsersOfEvent method, of class UserEventManager.
     */
    @Test
    public void testInvitedUsersOfEvent() throws Exception {
        assertEquals(user2.getEmail(), uem.invitedUsersOfEvent(event1).get(0));
        assertEquals((int)1, (int)uem.invitedUsersOfEvent(event1).size());
    }

    /**
     * Test of deleteUserEvent method, of class UserEventManager.
     */
    @Test
    public void testDeleteUserEvent() throws Exception {

        uem.deleteUserEvent(event1);
        assertNull(uem.invitedUsersOfEvent(event1));
    }

    /**
     * Test of getUsersCreator method, of class UserEventManager.
     */
    @Test
    public void testGetUsersCreator() throws Exception {

        List<Users> users = uem.getUsersCreator();
        assertEquals((int)users.size(), (int)1);
        assertEquals(users.get(0), user1);
    }

    /**
     * Test of getInvitedWhoAccepted method, of class UserEventManager.
     */
    @Test
    public void testGetInvitedWhoAccepted() throws Exception {
        assertNull(uem.getInvitedWhoAccepted(event1));
        uem.modifyUserEvent(userEvent1, true, true);
        assertEquals(user2, uem.getInvitedWhoAccepted(event1).get(0));
    }

    public Event setEvent(String title,String Description,String city,Users creator,Timestamp start,Timestamp end,boolean outdoor,boolean publicEvent)
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

       private void insertData() throws Exception {
        user1 = new Users();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USER);
        user1.setPublicCalendar(false);
        em.persist(user1);
        user2 = new Users();
        user2.setEmail("user2@mail.it");
        user2.setPassword("user2");
        user2.setGroupName(Group.USER);
        user2.setPublicCalendar(true);
        em.persist(user2);
        user3 = new Users();
        user3.setEmail("user3@mail.it");
        user3.setPassword("user3");
        user3.setGroupName(Group.USER);
        user3.setPublicCalendar(true);
        em.persist(user3);

        event1=setEvent("Title", "Description", "Milan", user1, new Timestamp(1111), new Timestamp(11122), true, true);
        emi.addEvent(event1, user1);

        userEvent1 = new UserEvent(event1, user1, true);
        uem.addUserEvent(userEvent1);
        userEvent2 = new UserEvent(event1, user2, false);
        uem.addUserEvent(userEvent2);


       }

       private void clearData() throws Exception {
        //em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from User").executeUpdate();
        em.createQuery("delete from Event").executeUpdate();
        em.createQuery("delete from UserEvent").executeUpdate();
        em.createQuery("delete from Place").executeUpdate();
    }

}
