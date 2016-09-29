package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.repository.PostRepository;
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
public class PostRepositoryTest extends ArquillianTest {

    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private PostRepository postRepository;
    @EJB
    private com.dolplads.repository.UserRepository UserRepository;
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
        int size = postRepository.findAll().size();

        Post post = getValidPost();
        post = postRepository.save(post);

        assertNotNull(post.getId());
        assertEquals(size + 1, postRepository.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        Post post = getValidPost();
        post = postRepository.save(post);

        assertNotNull(post.getId());
        assertNotNull(postRepository.findById(post.getId()));
        assertNull(postRepository.findById(100L));
    }

    @Test
    public void remove() throws Exception {
        int size = postRepository.findAll().size();

        Post post = getValidPost();
        post = postRepository.save(post);

        assertNotNull(post.getId());
        assertEquals(size + 1, postRepository.findAll().size());

        postRepository.remove(post);

        assertNull(postRepository.findById(post.getId()));
        assertEquals(size, postRepository.findAll().size());
    }

    @Test
    public void update() throws Exception {
        Post post = getValidPost();
        post = postRepository.save(post);

        post = postRepository.findById(post.getId());
        assertEquals("four", post.getText());

        post.setText("updated");
        post = postRepository.update(post);

        post = postRepository.findById(post.getId());
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
        post = postRepository.save(post);

        post = postRepository.findById(post.getId());
        User u = post.getUser();
        u.setUserName("newUserName");
        u = UserRepository.update(u);

        post = postRepository.findById(post.getId());
        assertEquals("newUserName", post.getUser().getUserName());
    }

    @Test
    public void findAll() throws Exception {
        Post post1 = getValidPost();
        Post post2 = getValidPost();

        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> posts = postRepository.findAll();
        assertEquals(2, posts.size());
    }

    @Test
    public void findAllPaginated() throws Exception {
        Post post1 = getValidPost();
        Post post2 = getValidPost();
        Post post3 = getValidPost();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        List<Post> posts = postRepository.findAllPaginated(1, 2);

        assertEquals(2, posts.size());
    }

    @Test
    public void findByUser() throws Exception {
        User user = getPersistedUser();
        Post post1 = getValidPost();
        Post post2 = getValidPost();

        post1.setUser(user);
        post2.setUser(user);
        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> posts = postRepository.findByUser(user.getId());
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
        return UserRepository.save(getValidUser());
    }

    private Post getValidPost() {
        return new Post(getPersistedUser(), "four");
    }
}