package com.dolplads.repository.specs;

import com.dolplads.model.User;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by dolplads on 29/09/16.
 */
public class UserSpecByUserName implements UserSpec {
    private String userName;
    private final String userNameProp = "userName";

    public UserSpecByUserName(String userName) {
        super();
        this.userName = userName;
    }

    @Override
    public boolean specified(User user) {
        return user.getUserName() != null && user.getUserName().equals(userName);

    }

    public Criterion toCriteria() {
        return Restrictions.eq(userNameProp, userName);
    }
}
