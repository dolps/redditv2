package com.dolplads.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by dolplads on 27/09/16.
 */
@Entity
@NoArgsConstructor
@Data
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private User user;

    @Size(min = 4, max = 255)
    private String text;

    private int upVotes;

    private int downvotes;

    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    private void onPersist() {
        createdAt = new Date();
    }

    public Post(User user, String text) {
        this.user = user;
        this.text = text;
    }
}
