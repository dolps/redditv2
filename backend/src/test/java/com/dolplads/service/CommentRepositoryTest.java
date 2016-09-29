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
import java.util.List;
import java.util.logging.Level;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 27/09/16.
 */
@Log
public class CommentRepositoryTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private CommentRepository commentRepository;
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

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findByUser(user.getId());

        assertEquals(2, comments.size());
    }

    @Test
    public void save() throws Exception {
        int size = commentRepository.findAll().size();

        Comment comment = getValidComment();
        comment = commentRepository.save(comment);

        assertNotNull(comment.getId());
        assertEquals(size + 1, commentRepository.findAll().size());
    }

    @Test
    public void findById() throws Exception {
        Comment comment = getValidComment();
        comment = commentRepository.save(comment);

        assertNotNull(comment.getId());
        assertNotNull(commentRepository.findById(comment.getId()));
        assertNull(commentRepository.findById(100L));
    }

    @Test
    public void remove() throws Exception {
        int size = commentRepository.findAll().size();

        Comment comment = getValidComment();
        comment = commentRepository.save(comment);

        assertNotNull(comment.getId());
        assertEquals(size + 1, commentRepository.findAll().size());

        commentRepository.remove(comment);

        assertNull(commentRepository.findById(comment.getId()));
        assertEquals(size, commentRepository.findAll().size());
    }

    @Test
    public void update() throws Exception {
        Comment comment = getValidComment();
        commentRepository.save(comment);

        comment = commentRepository.findById(comment.getId());
        assertEquals("comment", comment.getText());

        comment.setText("updated");
        comment = commentRepository.update(comment);

        comment = commentRepository.findById(comment.getId());

        assertEquals("updated", comment.getText());
    }

    @Test
    public void findAll() throws Exception {
        commentRepository.save(getValidComment());
        commentRepository.save(getValidComment());

        List<Comment> comments = commentRepository.findAll();
        assertEquals(2, comments.size());
    }

    @Test
    public void findAllPaginated() throws Exception {
        commentRepository.save(getValidComment());
        commentRepository.save(getValidComment());
        commentRepository.save(getValidComment());

        List<Comment> comments = commentRepository.findAllPaginated(1, 2);
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
        return postRepository.save(getValidPost());
    }

    private User getPersistedUser() {
        return UserRepository.save(getValidUser());
    }

}