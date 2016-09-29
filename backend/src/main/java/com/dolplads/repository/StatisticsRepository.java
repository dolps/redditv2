package com.dolplads.repository;

import com.dolplads.model.Post;
import com.dolplads.model.User;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * Created by dolplads on 27/09/16.
 */
@Stateless
@SuppressWarnings(value = "unchecked")
public class StatisticsRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<String> distinctCountries() {
        return entityManager.createNamedQuery(User.DISTINCT_COUNTRIES).getResultList();
    }

    public int numberOfPosts() {
        Long res = (Long) entityManager.createNamedQuery(Post.TOTAL_NUMBER).getSingleResult();

        return toIntExact(res);
    }

    public int numberOfPostsByCountry(String country) {
        Long res = (Long) entityManager
                .createNamedQuery(Post.TOTAL_NUMBER_BY_COUNTRY)
                .setParameter("country", country)
                .getSingleResult();

        return toIntExact(res);
    }

    public int numberOfUsers() {
        Long res = (Long) entityManager.createNamedQuery(User.NUMBER_OF_USERS).getSingleResult();

        return toIntExact(res);
    }

    public int numberOfUsersByCountry(String country) {
        Long res = (Long) entityManager
                .createNamedQuery(User.NUMBER_OF_USERS_BY_COUNTRY)
                .setParameter("country", country).getSingleResult();

        return toIntExact(res);
    }

    public List<User> mostActiveUsers(int max) {
        return entityManager
                .createNamedQuery(User.MOST_ACTIVE)
                .setMaxResults(max)
                .getResultList();
    }

    public int getNumberOfCommentsByUser(Long userId) {
        return (Integer) entityManager.createNamedQuery(User.NUMBER_OF_COMMENTS_BY_USER)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    public int getNumberOfPostsByUser(Long userId) {
        return (Integer) entityManager.createNamedQuery(User.NUMBER_OF_POSTS_BY_USER)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
