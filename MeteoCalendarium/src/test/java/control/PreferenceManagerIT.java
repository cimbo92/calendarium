/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Group;
import entity.IDEvent;
import entity.MainCondition;
import entity.Place;
import entity.Preference;
import entity.UserEvent;
import entity.Users;
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
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Alessandro
 */
@RunWith(Arquillian.class)
public class PreferenceManagerIT {



    @EJB
    EventManager um;
@EJB
PreferenceManager pm;


    @PersistenceContext
    EntityManager entityManager;

   Users user1;

    Users user2,user3;
    Event event1, event2;
    UserEvent userEvent1, userEvent2;

        Preference p1;
        Preference p2 ;
    int IDCount;

       @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                .addClass(PreferenceManager.class)
                .addPackage(Event.class.getPackage())
                .addPackage(Place.class.getPackage())
                .addPackage(IDEvent.class.getPackage())
                .addPackage(Preference.class.getPackage())
                .addPackage(Users.class.getPackage())
                .addPackage((UserEvent.class.getPackage()))
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    /**
     * Test of getPreferenceOfEvent method, of class PreferenceManager.
     */
    @Test
    public void testGetPreferenceOfEvent() throws Exception {
        List<String> pref = pm.getPreferenceOfEvent(event1);
assertNotNull(pref);
assertEquals(pref.get(0),"Rain");
    }

       @Before
    public void prepareTests() throws Exception {
        clearData();
        insertData();
        IDCount=0;
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
        user1.setGroupName(Group.USERS);
        user1.setPublicCalendar(false);

        event1=setEvent("Title", "Description", "Milan", user1, new Timestamp(1111), new Timestamp(11122), true, true);

        p1 = new Preference();
        p2 = new Preference();

        p1.setEvent(event1);
        p2.setEvent(event1);

        p1.setMain(new MainCondition("Rain"));
        p2.setMain(new MainCondition("Clouds"));

        pm.addPreference(p1);
        pm.addPreference(p2);
       }

       private void clearData() throws Exception {
        System.out.println("Dumping old records...");

    }


}
