package com.dolplads.service;

import com.dolplads.model.Address;
import com.dolplads.model.User;
import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import lombok.extern.java.Log;
import org.junit.*;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 26/09/16.
 */
@Log
public class UserServiceTest<T> extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private UserService userService;
    @Inject
    private Validator validator;

    @Before
    @After
    public void restoreDB() throws Exception {
        log.log(Level.INFO, "restoring DB");
        deleterEJB.deleteEntities(User.class);
    }

    @Test
    public void createValidUser() {
        User user = getValidUser();

        assertEquals("should give no errors", 0, validator.validate(user).size());
    }

    @Test
    public void createNonValidUser() {
        User user = new User("t", "dolpladsgmail.com", null, null, null);
        assertEquals("should give plenty of errors", 3, validator.validate(user).size());
    }

    @Test
    public void save() throws Exception {
        int size = userService.findAll().size();

        User user = getValidUser();
        user = userService.save(user);

        assertNotNull(user.getId());
        assertEquals(size + 1, userService.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        User user = getValidUser();
        user = userService.save(user);

        assertNotNull(user.getId());
        assertNotNull(userService.findById(user.getId()));
        assertNull(userService.findById(100L));
    }

    @Test
    public void remove() throws Exception {
        int size = userService.findAll().size();

        User user = getValidUser();
        user = userService.save(user);

        assertNotNull(user.getId());
        assertEquals(size + 1, userService.findAll().size());

        userService.remove(user);

        assertNull(userService.findById(user.getId()));
        assertEquals(size, userService.findAll().size());
    }

    @Test
    public void update() throws Exception {
        User user = getValidUser();
        user = userService.save(user);

        user = userService.findById(user.getId());
        assertEquals("thomas", user.getUserName());

        user.setUserName("newName");
        user = userService.update(user);

        user = userService.findById(user.getId());
        assertEquals("newName", user.getUserName());
    }

    @Test
    public void findAll() throws Exception {
        User user1 = getValidUser();
        User user2 = getValidUser();

        userService.save(user1);
        userService.save(user2);

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
    }

    @Test
    public void findAllPaginated() throws Exception {
        User user1 = getValidUser();
        User user2 = getValidUser();
        User user3 = getValidUser();

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);

        List<User> users = userService.findAllPaginated(1, 2);

        assertEquals(2, users.size());
    }

    ////////////////////////////////
    /////Private helper methods/////
    ////////////////////////////////

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");
        return new User("thomas", "dolplads@gmail.com", "password", calendar.getTime(), address);
    }

}