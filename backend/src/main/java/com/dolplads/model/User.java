package com.dolplads.model;

import com.dolplads.annotations.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dolplads on 26/09/16.
 */
@NamedQueries({
        @NamedQuery(name = User.DISTINCT_COUNTRIES, query = "select user.address.country from User user"),
        @NamedQuery(name = User.NUMBER_OF_USERS, query = "select count(user) from User user"),
        @NamedQuery(name = User.NUMBER_OF_USERS_BY_COUNTRY,
                query = "select count(user) from User user where user.address.country = :country"),
        @NamedQuery(name = User.FIND_POSTS, query = "select user.posts from User user where user.id=:userId")
})
@Entity
@NoArgsConstructor
@Data
@ToString
public class User {
    public static final String DISTINCT_COUNTRIES = "user_distinct_countries";
    public static final String NUMBER_OF_USERS = "user_number";
    public static final String NUMBER_OF_USERS_BY_COUNTRY = "user_number_by_country";
    public static final String MOST_ACTIVE = "most_active_by_posts_comments";
    public static final String FIND_POSTS = "user_posts";
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 20)
    private String userName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @Temporal(TemporalType.DATE)
    @Past
    private Date dateOfBirth;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @Embedded
    private Address address;

    // ..
    public User(@NotNull String userName, @NotNull String email, @NotNull String password, Date dateOfBirth, Address address) {
        setUserName(userName);
        setEmail(email);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
    }

    @PostConstruct
    public void init() {
        posts = new ArrayList<>();
    }

    public void addPost(Post post) {
        post.setUser(this);
        if (!posts.contains(post)) {
            posts.add(post);
        }
    }
}
