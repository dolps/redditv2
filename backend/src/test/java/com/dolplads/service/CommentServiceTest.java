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
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 27/09/16.
 */
@Log
public class CommentServiceTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private CommentService commentService;
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
        deleterEJB.deleteEntities(Comment.class);
    }


    @Test
    public void createValidComment() throws Exception {
        Comment comment = getValidComment();

        assertEquals("should be ready for persist", 0, validator.validate(comment).size());
    }

    /**
     * Note that even though passing a valid user,
     * the user must bee persisted first to make the comment persist to DB
     *
     * @throws Exception
     */
    @Test
    public void createNonValidComment() throws Exception {
        Comment comment = new Comment(getValidUser(), getValidPost(), "t");

        assertEquals("not ready for persist", 1, validator.validate(comment).size());
    }

    @Test
    public void findByUser() throws Exception {
        User user = getPersistedUser();

        Comment comment1 = getValidComment();
        comment1.setUser(user);

        Comment comment2 = getValidComment();
        comment2.setUser(user);

        commentService.save(comment1);
        commentService.save(comment2);

        List<Comment> comments = commentService.findByUser(user.getId());

        assertEquals(2, comments.size());
    }

    @Test
    public void save() throws Exception {
        int size = commentService.findAll().size();

        Comment comment = getValidComment();
        comment = commentService.save(comment);

        assertNotNull(comment.getId());
        assertEquals(size + 1, commentService.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        Comment comment = getValidComment();
        comment = commentService.save(comment);

        assertNotNull(comment.getId());
        assertNotNull(commentService.findById(comment.getId()));
        assertNull(commentService.findById(100L));
    }

    @Test
    public void remove() throws Exception {
        int size = commentService.findAll().size();

        Comment comment = getValidComment();
        comment = commentService.save(comment);

        assertNotNull(comment.getId());
        assertEquals(size + 1, commentService.findAll().size());

        commentService.remove(comment);

        assertNull(commentService.findById(comment.getId()));
        assertEquals(size, commentService.findAll().size());
    }

    @Test
    public void update() throws Exception {
        Comment comment = getValidComment();
        commentService.save(comment);

        comment = commentService.findById(comment.getId());
        assertEquals("comment", comment.getText());

        comment.setText("updated");
        comment = commentService.update(comment);

        comment = commentService.findById(comment.getId());

        assertEquals("updated", comment.getText());
    }

    @Test
    public void findAll() throws Exception {
        commentService.save(getValidComment());
        commentService.save(getValidComment());

        List<Comment> comments = commentService.findAll();
        assertEquals(2, comments.size());
    }

    @Test
    public void findAllPaginated() throws Exception {
        commentService.save(getValidComment());
        commentService.save(getValidComment());
        commentService.save(getValidComment());

        List<Comment> comments = commentService.findAllPaginated(1, 2);
        assertEquals(2, comments.size());
    }

    ////////////////////////////////
    /////Private helper methods/////
    ////////////////////////////////

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");

        return new User("thomas", "test@test.com", "password", calendar.getTime(), address);
    }

    private Post getValidPost() {
        return new Post(getPersistedUser(), "post");
    }

    private Comment getValidComment() {
        return new Comment(getPersistedUser(), getPersistedPost(), "comment");
    }

    private Post getPersistedPost() {
        return postService.save(getValidPost());
    }

    private User getPersistedUser() {
        return userService.save(getValidUser());
    }

}