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
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
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

    @PersistenceContext
    private EntityManager em;

    private Event ev1 = new Event();
    private Event ev2 = new Event();
    private Event ev3 = new Event();
    private Event ev4 = new Event();
    private Users us1 = new Users();
    private Users us2 = new Users();

   @Inject
            UserTransaction utx;

    @EJB
    private EventManager evm;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                 .addClass(EntityManager.class)
                .addPackage(Event.class.getPackage())
                .addPackage(Place.class.getPackage())
                .addPackage(IDEvent.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    int IDCount = 0;

    public Event setEvent(String title, String Description, String city, Users creator, Timestamp start, Timestamp end, boolean outdoor, boolean publicEvent) {
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

    public Users setUser(String mail, String password, boolean calPublic) {
        Users us = new Users();
        us.setEmail(mail);
        us.setPassword(password);
        us.setPublicCalendar(calPublic);
        us.setGroupName(Group.USERS);
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

    /**
     * Test of searchOverlapping method, of class EventManager.
     */
    @Test
    public void testSearchOverlapping() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, Exception  {
        this.clearData();
        utx.begin();
        em.joinTransaction();



         utx.commit();
        // clear the persistence context (first-level cache)
        em.clear();
        assertNotNull(evm.loadSpecificEvent(ev1.getIdEvent().getId()+""));
    }

        private void clearData() throws Exception {
              utx.begin();
        em.joinTransaction();

        //em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Users").executeUpdate();
        em.createQuery("delete from Event").executeUpdate();
        em.createQuery("delete from UserEvent").executeUpdate();
        em.createQuery("delete from Place").executeUpdate();
        utx.commit();
    }
}
