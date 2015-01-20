/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.sql.Timestamp;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author home
 */
public class EventManagerTest {

    @Test
    public void testOverlapping() {

        EventManager em = new EventManager();

        //First data
        Timestamp d1i = new Timestamp(1000);
        Timestamp d1e = new Timestamp(2000);

        //SecondDate
        Timestamp d2i = new Timestamp(1000);
        Timestamp d2e = new Timestamp(2000);

        //Overlapping pure
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(1001);
        d2e = new Timestamp(1999);

        //Overlapping inside
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(999);
        d2e = new Timestamp(1000);

        //Overlapping left point
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(999);
        d2e = new Timestamp(1200);

        //Overlapping left
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(2000);
        d2e = new Timestamp(2300);

        //Overlapping right point
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(1500);
        d2e = new Timestamp(2300);

        //Overlapping right
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(500);
        d2e = new Timestamp(3000);

        //Overlapping full
        assertTrue(em.totalOverlapping(d1i, d1e, d2i, d2e));

        //Legal
        d2i = new Timestamp(500);
        d2e = new Timestamp(999);

        //Legal creation left
        assertFalse(em.totalOverlapping(d1i, d1e, d2i, d2e));

        d2i = new Timestamp(2001);
        d2e = new Timestamp(3000);

        //Legal creation right
        assertFalse(em.totalOverlapping(d1i, d1e, d2i, d2e));
    }

}
