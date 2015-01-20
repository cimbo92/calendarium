/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Group;
import entity.User;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UserManagerTest {

    private UserManager cut;

    @Before
    public void setUp() {
        cut = new UserManager();
        cut.setEm(mock(EntityManager.class));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void newUsersShouldBelongToUserGroupAndSavedOnce() throws Exception {
        User newUser = new User();
        newUser.setEmail("prova");
        cut.save(newUser);
        assertThat(newUser.getGroupName(), is(Group.USER));

        verify(cut.getEm(), times(1)).persist(newUser);
    }
}
