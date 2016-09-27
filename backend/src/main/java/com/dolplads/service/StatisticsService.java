package com.dolplads.service;

import com.dolplads.model.Post;
import com.dolplads.model.User;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

/**
 * Created by dolplads on 27/09/16.
 */
// TODO: 27/09/16 FINISH THIS
@Singleton
@SuppressWarnings(value = "unchecked")
public class StatisticsService {
    @PersistenceContext
    private EntityManager entityManager;

    // set of countries users are from
    // number of posts in total
    // number of posts from a spesific country
    // number of users
    // number of users from a spesific country
    // top x users that wrote the most comments
    public List<String> distinctCountries() {
        return entityManager.createNamedQuery(User.DISTINCT_COUNTRIES).getResultList();
    }

    public int numberOfPosts() {
        return entityManager.createNamedQuery(Post.TOTAL_NUMBER).getFirstResult();
    }

    public int numberOfPostsByCountry(String country) {
        return entityManager
                .createNamedQuery(Post.TOTAL_NUMBER_BY_COUNTRY)
                .setParameter("country", country)
                .getFirstResult();
    }

    public int numberOfUsers() {
        return entityManager.createNamedQuery(User.NUMBER_OF_USERS).getFirstResult();
    }

    public int numberOfUsersByCountry(String country) {
        return entityManager
                .createNamedQuery(User.NUMBER_OF_USERS_BY_COUNTRY)
                .setParameter("country", country).getFirstResult();
    }

    public List<User> mostActiveUsers(int max) {
        return entityManager
                .createNamedQuery(User.MOST_ACTIVE)
                .setMaxResults(max)
                .getResultList();
    }

}
