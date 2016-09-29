package com.dolplads.repository;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Comment;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.repository.CommentRepository;
import com.dolplads.repository.PostRepository;
import com.dolplads.repository.StatisticsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.EJB;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dolplads on 27/09/16.
 */
public class StatisticsRepositoryTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private StatisticsRepository statisticsRepository;
    @EJB
    private com.dolplads.repository.UserRepository UserRepository;
    @EJB
    private PostRepository postRepository;
    @EJB
    private CommentRepository commentRepository;

    /**
     * Creating:
     * 3 users 2 from Norway, 1 from Sweden
     * 6 posts 2 from Norway, 4 from Sweden
     * Most active = user3 with 4 posts + 1 comment
     * 3 comments in total
     */
    @Before
    public void prepareTest() {
        prepareData();
    }

    @After
    public void restoreDB() {
        deleterEJB.deleteEntities(Comment.class);
        deleterEJB.deleteEntities(Post.class);
        deleterEJB.deleteEntities(User.class);
    }

    @Test
    public void distinctCountries() throws Exception {
        List<String> countries = statisticsRepository.distinctCountries();

        assertEquals(2, countries.size());
        assertTrue(countries.contains("Norway"));
        assertTrue(countries.contains("Sweden"));
    }

    @Test
    public void numberOfPosts() throws Exception {
        assertEquals(6, statisticsRepository.numberOfPosts());
    }

    @Test
    public void numberOfPostsByCountry() throws Exception {
        assertEquals(2, statisticsRepository.numberOfPostsByCountry("Norway"));
        assertEquals(4, statisticsRepository.numberOfPostsByCountry("Sweden"));
        assertEquals(0, statisticsRepository.numberOfPostsByCountry("Denmark"));
    }

    @Test
    public void numberOfUsers() throws Exception {
        assertEquals(3, statisticsRepository.numberOfUsers());
    }

    @Test
    public void numberOfUsersByCountry() throws Exception {
        assertEquals(2, statisticsRepository.numberOfUsersByCountry("Norway"));
        assertEquals(1, statisticsRepository.numberOfUsersByCountry("Sweden"));
        assertEquals(0, statisticsRepository.numberOfUsersByCountry("Denmark"));
    }

    @Test
    public void mostActiveUsers() throws Exception {
        List<User> orderedByActiveness = statisticsRepository.mostActiveUsers(2);
        User mostActive = orderedByActiveness.get(0);

        assertEquals("most active should be: ", "mostActive", mostActive.getUserName());
        assertTrue(orderedByActiveness.size() == 2);
        int numberOfPosts = statisticsRepository.getNumberOfPostsByUser(mostActive.getId());
        int numberOfComments = statisticsRepository.getNumberOfCommentsByUser(mostActive.getId());
        assertEquals(4,numberOfPosts);
        assertEquals(1,numberOfComments);
    }


    ////////////////////////////////
    /////Private helper methods/////
    ////////////////////////////////

    private User getValidUser() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address = new Address("street", "city", "country");
        return new User("thomas", "dolplads@gmail.com", "password", calendar.getTime(), address);
    }

    private Post getValidPost() {
        return new Post(null, "four");
    }


    private void prepareData() {
        Calendar calendar = new GregorianCalendar(1989, 7, 10);
        Address address1 = new Address("street", "city", "Norway");
        Address address2 = new Address("street", "city", "Norway");
        Address address3 = new Address("street", "city", "Sweden");

        User user1 = new User("Active", "dolplads@gmail.com", "password", calendar.getTime(), address1);
        User user2 = new User("thomas", "dolplads@gmail.com", "password", calendar.getTime(), address2);
        User user3 = new User("mostActive", "dolplads@gmail.com", "password", calendar.getTime(), address3);

        persistUsers(user2, user1, user3);
        assertEquals(3, UserRepository.findAll().size());

        Post post1 = new Post(user1, "post1");
        Post post2 = new Post(user1, "post2");
        Post post3 = new Post(user3, "post3");
        Post post4 = new Post(user3, "post3");
        Post post5 = new Post(user3, "post3");
        Post post6 = new Post(user3, "post3");

        persistPosts(post1, post2, post3, post4, post5, post6);// TODO: 27/09/16 FIX THIS
        assertEquals(6, postRepository.findAll().size());

        Comment comment1 = new Comment(user1, post1, "comment1");
        Comment comment2 = new Comment(user2, post1, "comment1");
        Comment comment3 = new Comment(user3, post1, "comment1");

        persistComments(comment1, comment2, comment3);
        assertEquals(3, commentRepository.findAll().size());
    }

    private void persistComments(Comment... comments) {
        for (Comment comment : comments) {
            commentRepository.save(comment);
        }
    }

    private void persistPosts(Post... posts) {
        for (Post post : posts) {
            postRepository.save(post);
        }
    }

    private void persistUsers(User... users) {
        for (User user : users) {
            UserRepository.save(user);
        }
    }

}