package com.transation.demo.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class DbConnMonitor {

    @Autowired
    private HikariDataSource hikari;

    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void printAvailableConnections() {

    }
}
