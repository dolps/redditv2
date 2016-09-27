package com.dolplads.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

/**
 * Created by dolplads on 27/09/16.
 */
@NamedQueries(
        @NamedQuery(name = Post.FIND_BY_USER, query = "select post from Post post where post.user.id = :userId")
)
@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Post extends UserContribution {
    public static final String FIND_BY_USER = "post_by_user";

    public Post(@NotNull User user, @NotNull String text) {
        setUser(user);
        setText(text);
    }
}
