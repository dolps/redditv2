package com.dolplads.service;

import com.dolplads.model.User;
import com.dolplads.repository.CrudRepository;
import lombok.extern.java.Log;

import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;

/**
 * Created by dolplads on 26/09/16.
 */
@Stateless
public class UserService extends CrudRepository<User> {

    public UserService() {
        super(User.class);
    }
}
