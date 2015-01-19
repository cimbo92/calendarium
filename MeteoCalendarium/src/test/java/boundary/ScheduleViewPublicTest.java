/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.EventManagerInterface;
import control.UserManagerInterface;
import entity.Event;
import entity.User;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alessandro
 */
public class ScheduleViewPublicTest {


      private ScheduleViewPublic s = new ScheduleViewPublic();
        User loggedUser = new User();
        User publicUser1 = new User();
        User publicUser2 = new User();
        User privateUser3 = new User();
        List<String> allUsers = new ArrayList<>();
        List<String> publicUsers = new ArrayList<>();
        Event eventToSee = new Event();
        Event eventPrivate = new Event();

    private User initUser(String name) {
        User u = new User();
        u.setEmail(name);
        u.setGroupName("USER");
        u.setPassword("test");
        u.setPublicCalendar(true);
        return u;
    }

    public void initUsersList(){
        loggedUser = this.initUser("pippo");
        publicUser1 = this.initUser("paperino");
        publicUser2 = this.initUser("pluto");
        privateUser3 = this.initUser("minny");
        privateUser3.setPublicCalendar(false);


        allUsers.add(loggedUser.getEmail());
        allUsers.add(publicUser1.getEmail());
        allUsers.add(publicUser2.getEmail());
        allUsers.add(privateUser3.getEmail());


        publicUsers.add(publicUser1.getEmail());
        publicUsers.add(publicUser2.getEmail());

    }

    private void initEvents(){
        eventToSee.setCreator(loggedUser);
       eventPrivate.setCreator(loggedUser);
    }

    private void setQueries(){
        UserManagerInterface um = mock(UserManagerInterface.class);
        EventManagerInterface em = mock(EventManagerInterface.class);
        s.em=em;
        s.um=um;
        s.init();

        when(um.getLoggedUser()).thenReturn(loggedUser);
        when(um.getListUsers()).thenReturn(allUsers);
        when(um.getListUsersPublic()).thenReturn(publicUsers);

    }


    private void initTest(){
        this.initUsersList();
        this.setQueries();
    }

    @Test
    public void initSchedule() {
    this.initTest();
    s.init();
    assertTrue(s.getUsers().contains(publicUser1.getEmail()));
    assertTrue(!s.getUsers().contains(privateUser3.getEmail()));

    }

  //  @Test
    //public void testCalendar{
      //  this.initTest();
       // s.init();

//        s.loadCalendar();
    




}
