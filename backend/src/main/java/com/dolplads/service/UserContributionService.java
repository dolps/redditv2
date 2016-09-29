package com.dolplads.service;

import com.dolplads.model.Comment;
import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.repository.CommentRepository;
import com.dolplads.repository.PostRepository;
import com.dolplads.repository.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by dolplads on 27/09/16.
 * <p>
 * Handles activites performed by users of the page
 * like placing a comment or a post
 */
@Stateless
public class UserContributionService {
    @EJB
    private PostRepository postRepository;
    @EJB
    private UserRepository userRepository;
    @EJB
    private CommentRepository commentRepository;

    public Post placePost(Long userId, Post post) {
        User user = userRepository.findById(userId);

        if (user != null) {
            post.setUser(user);
            return postRepository.save(post);
        }

        return null;
    }

    public Comment placeComment(Long userId, Long postId, Comment comment) {
        User user = userRepository.findById(userId);
        Post post = postRepository.findById(postId);

        if (user != null && post != null) {
            comment.setUser(user);
            comment.setPost(post);
            return commentRepository.save(comment);
        }
        return null;
    }
}
