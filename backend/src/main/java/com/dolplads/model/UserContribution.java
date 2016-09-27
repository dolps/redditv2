package com.dolplads.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by dolplads on 27/09/16.
 * User contributions are posts and comments made by the users
 */
@Data
@MappedSuperclass
@NoArgsConstructor
public abstract class UserContribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @Size(min = 4, max = 255)
    private String text;

    private int upVotes;

    private int downvotes;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    private void onPersist() {
        createdAt = new Date();
    }

    public UserContribution(@NotNull User user, String text) {
        setUser(user);
        setText(text);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
