package com.dolplads.model;

import com.dolplads.annotations.Persisted;
import lombok.Data;
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
@ToString
public class Post {
    public static final String FIND_BY_USER = "find_by_user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn

    @Persisted
    private User user;

    @Size(min = 4, max = 255)
    private String text;

    private int upVotes;

    private int downvotes;

    //@Past cannot be in the past.. my computer == to fast
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    private void onPersist() {
        createdAt = new Date();
    }

    public Post(@NotNull User user, String text) {
        setUser(user);
        setText(text);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
