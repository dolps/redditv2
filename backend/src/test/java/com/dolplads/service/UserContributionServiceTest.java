package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Comment;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.repository.CommentRepository;
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
    private PostRepository postRepository;
    @EJB
    private com.dolplads.repository.UserRepository UserRepository;
    @EJB
    private CommentRepository commentRepository;
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
        int size = postRepository.findAll().size();

        userContributionService.placePost(u.getId(), p1);
        userContributionService.placePost(u.getId(), p2);

        assertEquals(size + 2, postRepository.findAll().size());
    }

    // TODO: 27/09/16  IS THIS REDUNDANT SINCE WE ARE TESTING REMOVAL IN POST SERVICE
    @Test
    public void removePost() throws Exception {
        int size = postRepository.findAll().size();

        User u = getPersistedUser();
        Post p1 = getValidPost();
        p1.setUser(u);

        Post persistedPost = userContributionService.placePost(u.getId(), p1);

        assertEquals(size + 1, postRepository.findAll().size());

        persistedPost = postRepository.findById(persistedPost.getId());
        postRepository.remove(persistedPost);

        assertEquals("post should have been removed", size, postRepository.findAll().size());
        assertEquals("check on both sides", 0, postRepository.findByUser(u.getId()).size());
    }

    // TODO: 27/09/16  IS THIS REDUNDANT SINCE WE ARE TESTING REMOVAL IN COMMENT SERVICE
    @Test
    public void placeComment() throws Exception {
        int size = commentRepository.findAll().size();

        User persistedUser = getPersistedUser();
        Post persistedPost = getPersistedPost();


        userContributionService.placeComment(persistedUser.getId(), persistedPost.getId(), getValidComment());
        userContributionService.placeComment(persistedUser.getId(), persistedPost.getId(), getValidComment());

        assertEquals("size should increment", size + 2, commentRepository.findAll().size());
    }

    @Test
    public void removeComment() throws Exception {
        int size = commentRepository.findAll().size();

        User persistedUser = getPersistedUser();
        Post persistedPost = getPersistedPost();

        Comment persistedComment =
                userContributionService.placeComment(persistedUser.getId(), persistedPost.getId(), getValidComment());

        assertEquals(size + 1, commentRepository.findAll().size());

        persistedComment = commentRepository.findById(persistedComment.getId());
        commentRepository.remove(persistedComment);

        assertEquals("comment should have been removed", size, commentRepository.findAll().size());
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

    private Post getPersistedPost() {
        Post post = new Post(getPersistedUser(), "text");
        return postRepository.save(post);
    }

    private Post getValidPost() {
        return new Post(null, "four");
    }

    private Comment getValidComment() {
        return new Comment(getValidUser(), getValidPost(), "four");
    }

}