package com.dolplads.service;

import com.dolplads.model.Comment;
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
    //@PersistenceContext
    //private EntityManager entityManager;
    @EJB
    private PostService postService;
    @EJB
    private UserService userService;
    @EJB
    private CommentService commentService;

    public Post placePost(Long userId, Post post) {
        User user = userService.findById(userId);

        if (user != null) {
            post.setUser(user);
            user.addPost(post);
            return postService.save(post);
        }

        return null;
    }

    @SuppressWarnings(value = "unchecked")
    public List<Post> getPostsByUser(Long id) {
        /*
        return entityManager.createNamedQuery(User.FIND_POSTS)
                .setParameter("userId", id)
                .getResultList();
                */
        return postService.findByUser(id);
    }

    public Comment placeComment(Long userId, Long postId, Comment comment) {
        User user = userService.findById(userId);
        Post post = postService.findById(postId);

        if (user != null && post != null) {
            comment.setUser(user);
            comment.setPost(post);
            return commentService.save(comment);
        }
        return null;
    }
}
