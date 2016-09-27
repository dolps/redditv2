package com.dolplads.service;

import com.dolplads.model.Post;
import com.dolplads.model.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by dolplads on 27/09/16.
 */
@Stateless
public class UserContributionService {
    @PersistenceContext
    private EntityManager entityManager;
    @EJB
    private PostService postService;
    @EJB
    private UserService userService;

    public Post placePost(Long userId, Post post) {
        User user = entityManager.find(User.class, userId);

        if (user != null) {
            post.setUser(user);
            user.addPost(post);
            return postService.save(post);
        }

        return null;
    }

    @SuppressWarnings(value = "unchecked")
    public List<Post> getPostsByUser(Long id) {
        return entityManager.createQuery("select user.posts from User user where user.id=:userId")
                .setParameter("userId", id)
                .getResultList();
    }
}
