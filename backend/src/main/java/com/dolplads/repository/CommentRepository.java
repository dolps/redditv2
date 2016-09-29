package com.dolplads.repository;

import com.dolplads.model.Comment;
import com.dolplads.model.Post;
import com.dolplads.repository.CrudRepository;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by dolplads on 27/09/16.
 */
@Stateless
public class CommentRepository extends CrudRepository<Comment> {
    public CommentRepository() {
        super(Comment.class);
    }
    @SuppressWarnings(value = "unchecked")
    public List<Comment> findByUser(Long userId) {
        return entityManager.createNamedQuery(Comment.FIND_BY_USER)
                .setParameter("userId", userId)
                .getResultList();
    }
}
