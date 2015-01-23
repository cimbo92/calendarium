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
import javax.ejb.embeddable.EJBContainer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author alessandro
 */
@RunWith(Arquillian.class)
public class UserManagerIT {

    public UserManagerIT() {
    }

    @EJB
    private UserManagerInterface um;
    @PersistenceContext
    private EntityManager em;

    @Inject
    Principal principal;

    Users user1;
    Users user2;

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManagerInterface.class)
                .addClass(EntityManager.class)
                .addPackage(Users.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Before
    public void prepareTests() throws Exception {
        um = new UserManager();
        um.setEm(em);
        clearData();
        insertData();
    }

    /**
     * Test of getListUsers method, of class UserManager.
     */
    @Test
    public void testGetListUsers() throws Exception {
        List<String> users = um.getListUsersPublic();
        assertEquals(um.findByMail(users.get(0)), user1);
        assertEquals(um.findByMail(users.get(1)), user2);
    }

    /**
     * Test of setCalendar method, of class UserManager.
     */
    @Test
    public void testSetCalendar() throws Exception {
        Users user = um.findByMail("user1@mail.it");
        assertEquals(false, user.isPublicCalendar());
        um.setCalendar(true, user);
        assertEquals(true, user.isPublicCalendar());
    }

    /**
     * Test of getListUsersPublic method, of class UserManager.
     */
    @Test
    public void testGetListUsersPublic() throws Exception {
        List<String> users = um.getListUsersPublic();
        assertEquals("user2@mail.it", um.findByMail(users.get(0)));
    }

    /**
     * Test of save method, of class UserManager.
     */
    @Test
    public void testSave() throws Exception {

        Users user1 = new Users();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USER);
        user1.setPublicCalendar(true);
        um.save(user1);
        assertEquals(user1, um.findByMail(user1.getEmail()));
    }

    private void clearData() throws Exception {
        //em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from User").executeUpdate();
    }

    private void insertData() throws Exception {
        user1 = new Users();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USER);
        user1.setPublicCalendar(false);
        em.persist(user1);
        user2 = new Users();
        user2.setEmail("user2@mail.it");
        user2.setPassword("user2");
        user2.setGroupName(Group.USER);
        user2.setPublicCalendar(true);
        em.persist(user2);
    }
}
