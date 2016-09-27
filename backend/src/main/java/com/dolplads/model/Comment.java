package com.dolplads.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dolplads on 27/09/16.
 */
@NamedQueries(
        @NamedQuery(name = Comment.FIND_BY_USER, query = "select c from Comment c where c.user.id = :userId")
)
@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Comment extends UserContribution {
    public static final String FIND_BY_USER = "comment_by_user";

    @ManyToOne
    @JoinColumn
    private Post post;

    public Comment(@NotNull User user, @NotNull Post post, String text) {
        super(user, text);
        this.post = post;
    }
}
