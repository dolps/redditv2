package com.dolplads.repository;

import com.dolplads.model.User;

import javax.ejb.Stateless;

/**
 * Created by dolplads on 26/09/16.
 */
@Stateless
public class UserRepository extends CrudRepository<User> {
    public UserRepository() {
        super(User.class);
    }
}
