/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.UserManager;
import entity.Users;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author alessandro
 */
public class SetCalendarBeanTest {

    /**
     * Test of isStatus method, of class SetCalendarBean.
     */
    @Test
    public void testIsStatus() {

        UserManager um = mock(UserManager.class);

        SetCalendarBean sc = new SetCalendarBean();

        Users userTest = new Users();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        when(um.getLoggedUser()).thenReturn(userTest);

        sc.um = um;

        assertEquals(sc.isStatus(), userTest.isPublicCalendar());

    }

}
