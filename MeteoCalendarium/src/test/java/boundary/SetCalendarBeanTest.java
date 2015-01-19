/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.EventManagerInterface;
import control.UserManagerInterface;
import entity.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
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
      
        UserManagerInterface um = mock(UserManagerInterface.class);
        
        SetCalendarBean sc = new SetCalendarBean();
        
        User userTest = new User();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);
        
          when(um.getLoggedUser()).thenReturn(userTest);
          
          sc.um=um;
          
          assertEquals( sc.isStatus() , userTest.isPublicCalendar() );
      
    }
    
}
