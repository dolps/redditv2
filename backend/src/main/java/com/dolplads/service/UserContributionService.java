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
 * <p>
 * Handles activites performed by users of the page
 * like placing a comment or a post
 */
@Stateless
public class UserContributionService {
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
            return postService.save(post);
        }

        return null;
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
