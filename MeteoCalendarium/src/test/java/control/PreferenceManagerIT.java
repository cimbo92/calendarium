/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.IDEvent;
import entity.Place;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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

    @PersistenceContext
    EntityManager entityManager;



       @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                .addPackage(Event.class.getPackage())
                .addPackage(Place.class.getPackage())
                .addPackage(IDEvent.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    /**
     * Test of getPreferenceOfEvent method, of class PreferenceManager.
     */
    @Test
    public void testGetPreferenceOfEvent() throws Exception {

    }

}
