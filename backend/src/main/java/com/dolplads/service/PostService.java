package com.dolplads.service;

import com.dolplads.model.Post;
import com.dolplads.repository.CrudRepository;
import lombok.extern.java.Log;

import javax.ejb.Stateless;

/**
 * Created by dolplads on 27/09/16.
 */
@Stateless
@Log
public class PostService extends CrudRepository<Post> {
    public PostService() {
        super(Post.class);
    }
}
