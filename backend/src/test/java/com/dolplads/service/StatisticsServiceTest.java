package com.dolplads.service;

import com.dolplads.helpers.ArquillianTest;
import com.dolplads.helpers.DeleterEJB;
import com.dolplads.model.Address;
import com.dolplads.model.Comment;
import com.dolplads.model.Post;
import com.dolplads.model.User;
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
public class StatisticsServiceTest extends ArquillianTest {
    @EJB
    private DeleterEJB deleterEJB;
    @EJB
    private StatisticsService statisticsService;
    @EJB
    private UserService userService;
    @EJB
    private PostService postService;
    @EJB
    private CommentService commentService;

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
        List<String> countries = statisticsService.distinctCountries();

        assertEquals(2, countries.size());
        assertTrue(countries.contains("Norway"));
        assertTrue(countries.contains("Sweden"));
    }

    @Test
    public void numberOfPosts() throws Exception {
        assertEquals(6, statisticsService.numberOfPosts());
    }

    @Test
    public void numberOfPostsByCountry() throws Exception {
        assertEquals(2, statisticsService.numberOfPostsByCountry("Norway"));
        assertEquals(4, statisticsService.numberOfPostsByCountry("Sweden"));
        assertEquals(0, statisticsService.numberOfPostsByCountry("Denmark"));
    }

    @Test
    public void numberOfUsers() throws Exception {
        assertEquals(3, statisticsService.numberOfUsers());
    }

    @Test
    public void numberOfUsersByCountry() throws Exception {
        assertEquals(2, statisticsService.numberOfUsersByCountry("Norway"));
        assertEquals(1, statisticsService.numberOfUsersByCountry("Sweden"));
        assertEquals(0, statisticsService.numberOfUsersByCountry("Denmark"));
    }

    @Test
    public void mostActiveUsers() throws Exception {
        List<User> orderedByActiveness = statisticsService.mostActiveUsers(2);
        User mostActive = orderedByActiveness.get(0);

        assertEquals("most active should be: ", "mostActive", mostActive.getUserName());
        assertTrue(orderedByActiveness.size() == 2);
        int numberOfPosts = statisticsService.getNumberOfPostsByUser(mostActive.getId());
        int numberOfComments = statisticsService.getNumberOfCommentsByUser(mostActive.getId());
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
        assertEquals(3, userService.findAll().size());

        Post post1 = new Post(user1, "post1");
        Post post2 = new Post(user1, "post2");
        Post post3 = new Post(user3, "post3");
        Post post4 = new Post(user3, "post3");
        Post post5 = new Post(user3, "post3");
        Post post6 = new Post(user3, "post3");

        persistPosts(post1, post2, post3, post4, post5, post6);// TODO: 27/09/16 FIX THIS
        assertEquals(6, postService.findAll().size());

        Comment comment1 = new Comment(user1, post1, "comment1");
        Comment comment2 = new Comment(user2, post1, "comment1");
        Comment comment3 = new Comment(user3, post1, "comment1");

        persistComments(comment1, comment2, comment3);
        assertEquals(3, commentService.findAll().size());
    }

    private void persistComments(Comment... comments) {
        for (Comment comment : comments) {
            commentService.save(comment);
        }
    }

    private void persistPosts(Post... posts) {
        for (Post post : posts) {
            postService.save(post);
        }
    }

    private void persistUsers(User... users) {
        for (User user : users) {
            userService.save(user);
        }
    }

}