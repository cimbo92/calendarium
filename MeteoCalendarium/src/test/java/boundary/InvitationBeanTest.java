/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import HelpClasses.EventCreation;
import control.EventManagerInterface;
import control.UserManagerInterface;
import entity.Event;
import entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Alessandro
 */
public class InvitationBeanTest {


    Query query = mock(Query.class);

    private EventCreation init(String title){
          EventCreation e1 = new EventCreation();
          e1.setTitle(title);
          return e1;
    }

    private Event initEvent(String title){
          Event e1 = new Event();
          e1.setTitle(title);
          return e1;
    }

    @Test
    public void invitationInList(){
        EventManagerInterface em = mock(EventManagerInterface.class);
        UserManagerInterface um = mock(UserManagerInterface.class);
        InvitationBean ib = new InvitationBean();

        User userTest = new User();
        userTest.setEmail("gigi@mail.it");
        userTest.setGroupName("USERS");
        userTest.setPassword("pippo");
        userTest.setPublicCalendar(true);

        List<Event> list2 = new ArrayList<>();
        Event eventtemp1 = this.initEvent("prova1");
        Event eventtemp2 = this.initEvent("prova2");

        List<EventCreation> list = new ArrayList<>();
        EventCreation e1 = this.init("prova1");
        EventCreation e2 = this.init("prova2");

        list.add(e1);
        list.add(e2);

        list2.add(eventtemp1);
        list2.add(eventtemp2);

     //  when(um.getLoggedUser()).thenReturn(userTest);
        when(em.findInvitatedEvent((User) (Matchers.anyObject()))).thenReturn(list2);
       // when(query.setParameter(Matchers.anyString(), Matchers.anyObject())).thenReturn(query);
        //when(query.getResultList()).thenReturn(list);

        ib.um=um;
        ib.em=em;
        ib.setInvites(new ArrayList<EventCreation>());
        ib.loadInvites();
        List<EventCreation> temp = ib.getInvites();

        assertTrue(temp.get(0).getTitle().equals(e1.getTitle()));
    }
}
