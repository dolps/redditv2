package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.service.PostService;
import com.dolplads.service.UserContributionService;
import com.dolplads.service.UserService;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 27/09/16.
 */
@Log
public class UserContributionServiceTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @Inject
    private Validator validator;
    @EJB
    private PostService postService;
    @EJB
    private UserService userService;
    @EJB
    private UserContributionService userContributionService;

    @Before
    @After
    public void restoreDB() throws Exception {
        log.log(Level.INFO, "restoring DB");
        deleterEJB.deleteEntities(Post.class);
    }


    @Test
    public void createPost() throws Exception {
        User u = getPersistedUser();
        Post p1 = getValidPost();
        Post p2 = getValidPost();

        int size = postService.findAll().size();

        Post persisted1 = userContributionService.createWithUser(u.getId(), p1);
        Post persisted2 = userContributionService.createWithUser(u.getId(), p2);

        assertEquals(size + 2, postService.findAll().size());
    }

    // TODO: 27/09/16 REFACTORING NEEDED!
    @Test
    public void removePost() throws Exception {
        int size = postService.findAll().size();

        User u = getPersistedUser();
        Post p1 = getValidPost();
        p1.setUser(u);
        Post p2 = getValidPost();
        p2.setUser(u);

        Post persistedPost = userContributionService.createWithUser(u.getId(), p1);
        userContributionService.createWithUser(u.getId(), p2);
        assertEquals(size + 2, postService.findAll().size());

        postService.remove(persistedPost);

        assertEquals("one post has been removed", size + 1, postService.findAll().size());
        assertEquals("check on both sides", 1, postService.findByUser(u.getId()).size());
        assertEquals("check that its been managed on both sides", 1, userContributionService.getPostsByUser(u.getId()).size());
    }

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");
        User user = new User("thomas", "test@test.com", "password", calendar.getTime(), address);
        return user;
    }

    private User getPersistedUser() {
        return userService.save(getValidUser());
    }

    public Post getValidPost() {
        Post post = new Post(getPersistedUser(), "four");
        return post;
    }

}