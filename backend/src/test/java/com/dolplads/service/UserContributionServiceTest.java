package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Comment;
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
    private CommentService commentService;
    @EJB
    private UserContributionService userContributionService;

    @Before
    @After
    public void restoreDB() throws Exception {
        log.log(Level.INFO, "restoring DB");
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(Post.class);
        deleterEJB.deleteEntities(User.class);
    }


    @Test
    public void createPost() throws Exception {
        User u = getPersistedUser();
        Post p1 = getValidPost();
        Post p2 = getValidPost();

        p1.setUser(u);
        p2.setUser(u);
        int size = postService.findAll().size();

        userContributionService.placePost(u.getId(), p1);
        userContributionService.placePost(u.getId(), p2);

        assertEquals("check managed on both sides", size + 2, postService.findAll().size());
        // // TODO: 27/09/16 should remove this swapping implementation
        assertEquals("check managed on both sides", size + 2, userContributionService.getPostsByUser(u.getId()).size());
    }

    @Test
    public void removePost() throws Exception {
        int size = postService.findAll().size();

        User u = getPersistedUser();
        Post p1 = getValidPost();
        p1.setUser(u);

        Post persistedPost = userContributionService.placePost(u.getId(), p1);

        assertEquals(size + 1, postService.findAll().size());

        persistedPost = postService.findById(persistedPost.getId());
        postService.remove(persistedPost);

        assertEquals("one post has been removed", size, postService.findAll().size());
        assertEquals("check on both sides", 0, postService.findByUser(u.getId()).size());
        assertEquals("check that its been managed on both sides", 0, userContributionService.getPostsByUser(u.getId()).size());
    }

    @Test
    public void placeComment() throws Exception {
        int size = commentService.findAll().size();

        User persistedUser = getPersistedUser();
        Post persistedPost = getPersistedPost();


        userContributionService.placeComment(persistedUser.getId(), persistedPost.getId(), getValidComment());
        userContributionService.placeComment(persistedUser.getId(), persistedPost.getId(), getValidComment());

        assertEquals("size should increment", size + 2, commentService.findAll().size());
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

    private Post getPersistedPost() {
        Post post = new Post(getPersistedUser(), "text");
        return postService.save(post);
    }

    public Post getValidPost() {
        Post post = new Post(null, "four");
        return post;
    }

    public Comment getValidComment() {
        return new Comment(getValidUser(), getValidPost(), "four");
    }

}