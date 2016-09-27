package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by dolplads on 27/09/16.
 */
@Log
public class PostServiceTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private PostService postService;
    @Inject
    private Validator validator;

    @Before
    @After
    public void restoreDB() throws Exception {
        log.log(Level.INFO, "restoring DB");
        deleterEJB.deleteEntities(Post.class);
    }

    @Test
    public void createValidPost() throws Exception {
        Post post = getValidPost();

        assertEquals("should be ready for persist", 0, validator.validate(post).size());
    }

    @Test
    public void createNonValidPost() throws Exception {
        Post post = new Post(null, "tre");

        assertEquals("not ready for persist", 2, validator.validate(post).size());
    }

    @Test
    public void save() throws Exception {
        int size = postService.findAll().size();
        
        Post post = getValidPost();

        postService.save(post);

        assertNotNull(post.getId());
        assertEquals(size + 1, postService.findAll().size());
    }

    @Test
    public void findById() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void findAll() throws Exception {

    }

    @Test
    public void findAllPaginated() throws Exception {

    }

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");
        return new User("thomas", "dolplads@gmail.com", "password", calendar.getTime(), address);
    }

    public Post getValidPost() {
        Post post = new Post(getValidUser(), "four");
        return post;
    }
}