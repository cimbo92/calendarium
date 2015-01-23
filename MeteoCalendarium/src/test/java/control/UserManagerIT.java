/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Group;
import entity.User;
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
    
    
    private UserManager cut;
    
    @Deployment
    public static WebArchive createArchiveAndDeploy(){
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManagerInterface.class)
                .addClass(EntityManager.class)     
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void prepareTests() throws Exception {
        clearData();
        insertData();
    }
    
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

    /**
     * Test of getListUsers method, of class UserManager.
     *//*
    @Test
    public void testGetListUsers() throws Exception {
        System.out.println("getListUsers");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        UserManagerInterface instance = (UserManagerInterface)container.getContext().lookup("java:global/classes/UserManager");
        List<String> expResult = null;
        List<String> result = instance.getListUsers();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

   

    /**
     * Test of setCalendar method, of class UserManager.
     *//*
    @Test
    public void testSetCalendar() throws Exception {
        System.out.println("setCalendar");
        boolean status = false;
        User user = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        UserManagerInterface instance = (UserManagerInterface)container.getContext().lookup("java:global/classes/UserManager");
        instance.setCalendar(status, user);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    

    /**
     * Test of getListUsersPublic method, of class UserManager.
     *//*
    @Test
    public void testGetListUsersPublic() throws Exception {
        System.out.println("getListUsersPublic");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        UserManagerInterface instance = (UserManagerInterface)container.getContext().lookup("java:global/classes/UserManager");
        List<String> expResult = null;
        List<String> result = instance.getListUsersPublic();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/



    /**
     * Test of save method, of class UserManager.
     */
    @Test
    public void testSave() throws Exception {
     
        User user1 = new User();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USER);
        user1.setPublicCalendar(true);
        um.save(user1);
        assertEquals(user1,um.findByMail(user1.getEmail()));
    }



    /**
     * Test of getLoggedUser method, of class UserManager.
     *//*
    @Test
    public void testGetLoggedUser() throws Exception {
        System.out.println("getLoggedUser");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        UserManagerInterface instance = (UserManagerInterface)container.getContext().lookup("java:global/classes/UserManager");
        User expResult = null;
        User result = instance.getLoggedUser();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */
    
     private void clearData() throws Exception {
        //em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from User").executeUpdate();
    }
    
      private void insertData() throws Exception {
        User user1 = new User();
        user1.setEmail("user1@mail.it");
        user1.setPassword("user1");
        user1.setGroupName(Group.USER);
        em.persist(user1);
      }    
}
