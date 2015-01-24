/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Group;
import entity.Users;
import java.security.Principal;
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
public class UserManagerIT {

    public UserManagerIT() {
    }

    @EJB
    private UserManager um;
    @PersistenceContext
    private EntityManager em;
    @EJB
    private DestroyerManager dm;

    @Inject
    Principal principal;

    Users user1;
    Users user2;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addClass(EntityManager.class)
                .addClass(DestroyerManager.class)
                .addPackage(Users.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Before
    public void prepareTests() throws Exception {
        clearData();
        insertData();
    }

    /**
     * Test of getListUsers method, of class UserManager.
     */
    @Test
    public void testGetListUsers() throws Exception {
        List<String> users = um.getListUsersPublic();
        assertEquals(um.findByMail(users.get(0)).getEmail(), user2.getEmail());
        boolean errorpublic = users.contains(user1.getEmail());
        assertTrue(!errorpublic);
    }

    /**
     * Test of setCalendar method, of class UserManager.
     */
    @Test
    public void testSetCalendar() throws Exception {
        Users user = um.findByMail("user1@mail.it");
        assertEquals(false, user.isPublicCalendar());
        um.setCalendar(true, user);
        user = um.findByMail("user1@mail.it");
        assertEquals(true, user.isPublicCalendar());
    }

    /**
     * Test of getListUsersPublic method, of class UserManager.
     */
    @Test
    public void testGetListUsersPublic() throws Exception {
        List<String> users = um.getListUsersPublic();
        assertEquals("user2@mail.it", um.findByMail(users.get(0)).getEmail());
    }

    /**
     * Test of save method, of class UserManager.
     */
    @Test
    public void testSave() throws Exception {
        assertEquals(user1.getEmail(), um.findByMail(user1.getEmail()).getEmail());
    }


    private void insertData() throws Exception {
        user1 = new Users();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USERS);
        user1.setPublicCalendar(false);
        um.save(user1);
        user2 = new Users();
        user2.setEmail("user2@mail.it");
        user2.setPassword("user2");
        user2.setGroupName(Group.USERS );
        user2.setPublicCalendar(true);
        um.save(user2);
    }


    private void clearData(){
   dm.deleteAllData();
    }
}
