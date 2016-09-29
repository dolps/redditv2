package com.dolplads.repository;

import com.dolplads.model.Post;
import com.dolplads.model.User;
import com.dolplads.repository.CrudRepository;
import lombok.extern.java.Log;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by dolplads on 27/09/16.
 */
@Stateless
public class PostRepository extends CrudRepository<Post> {
    public PostRepository() {
        super(Post.class);
    }

    @SuppressWarnings(value = "unchecked")
    public List<Post> findByUser(Long userId) {
        return entityManager.createNamedQuery(Post.FIND_BY_USER)
                .setParameter("userId", userId)
                .getResultList();
    }
}
