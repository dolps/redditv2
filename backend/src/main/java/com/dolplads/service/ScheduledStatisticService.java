package com.dolplads.service;

import com.dolplads.model.User;
import com.dolplads.repository.StatisticsRepository;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

/**
 * Created by dolplads on 27/09/16.
 */
// TODO: 27/09/16 Write tests
@Singleton
@Data
public class ScheduledStatisticService {
    @EJB
    private StatisticsRepository statisticsRepository;
    private int numberOfPosts;
    private int numberOfUsers;
    private List<String> distinctCountries;
    private List<User> mostActiveUsers;

    @PostConstruct
    //@Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void updateStatistics() {
        numberOfPosts = statisticsRepository.numberOfPosts();
        numberOfUsers = statisticsRepository.numberOfUsers();
        distinctCountries = statisticsRepository.distinctCountries();
        mostActiveUsers = statisticsRepository.mostActiveUsers(10);
    }
}
