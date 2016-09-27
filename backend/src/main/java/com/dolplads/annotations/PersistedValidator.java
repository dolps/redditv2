package com.dolplads.annotations;

import com.dolplads.model.Post;
import com.dolplads.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by dolplads on 27/09/16.
 */
public class PersistedValidator implements ConstraintValidator<Persisted, User> {
    @Override
    public void initialize(Persisted constraint) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        return user.getId() != null;
    }
}
