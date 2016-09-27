package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import lombok.extern.java.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 27/09/16.
 */
@Log
public class PostServiceTest extends ArquillianTest {

    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private PostService postService;
    @EJB
    private UserService userService;
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


    /**
     * Note that even though passing a valid user,
     * the user must bee persisted first to make the post persist to DB
     *
     * @throws Exception
     */
    @Test
    public void createNonValidPost() throws Exception {
        Post post = new Post(getValidUser(), "tre");

        assertEquals("not ready for persist", 1, validator.validate(post).size());
    }

    @Test
    public void save() throws Exception {
        int size = postService.findAll().size();

        Post post = getValidPost();
        post = postService.save(post);

        assertNotNull(post.getId());
        assertEquals(size + 1, postService.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        Post post = getValidPost();
        post = postService.save(post);

        assertNotNull(post.getId());
        assertNotNull(postService.findById(post.getId()));
        assertNull(postService.findById(100L));
    }

    @Test
    public void remove() throws Exception {
        int size = postService.findAll().size();

        Post post = getValidPost();
        post = postService.save(post);

        assertNotNull(post.getId());
        assertEquals(size + 1, postService.findAll().size());

        postService.remove(post);

        assertNull(postService.findById(post.getId()));
        assertEquals(size, postService.findAll().size());
    }

    @Test
    public void update() throws Exception {
        Post post = getValidPost();
        post = postService.save(post);

        post = postService.findById(post.getId());
        assertEquals("four", post.getText());

        post.setText("updated");
        post = postService.update(post);

        post = postService.findById(post.getId());
        assertEquals("updated", post.getText());
    }

    /**
     * Not really necessary but wanted to be sure
     *
     * @throws Exception
     */
    @Test
    public void updateUserReflectsInPost() throws Exception {
        Post post = getValidPost();
        post = postService.save(post);

        post = postService.findById(post.getId());
        User u = post.getUser();
        u.setUserName("newUserName");
        u = userService.update(u);

        post = postService.findById(post.getId());
        assertEquals("newUserName", post.getUser().getUserName());
    }

    @Test
    public void findAll() throws Exception {
        Post post1 = getValidPost();
        Post post2 = getValidPost();

        postService.save(post1);
        postService.save(post2);

        List<Post> posts = postService.findAll();
        assertEquals(2, posts.size());
    }

    @Test
    public void findAllPaginated() throws Exception {
        Post post1 = getValidPost();
        Post post2 = getValidPost();
        Post post3 = getValidPost();

        postService.save(post1);
        postService.save(post2);
        postService.save(post3);

        List<Post> posts = postService.findAllPaginated(1, 2);

        assertEquals(2, posts.size());
    }

    @Test
    public void findByUser() throws Exception {
        User user = getPersistedUser();
        Post post1 = getValidPost();
        Post post2 = getValidPost();

        post1.setUser(user);
        post2.setUser(user);
        postService.save(post1);
        postService.save(post2);

        List<Post> posts = postService.findByUser(user.getId());
        assertEquals(2, posts.size());
    }

    ////////////////////////////////
    /////Private helper methods/////
    ////////////////////////////////

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");
        return new User("thomas", "test@test.com", "password", calendar.getTime(), address);
    }

    private User getPersistedUser() {
        return userService.save(getValidUser());
    }

    private Post getValidPost() {
        return new Post(getPersistedUser(), "four");
    }
}