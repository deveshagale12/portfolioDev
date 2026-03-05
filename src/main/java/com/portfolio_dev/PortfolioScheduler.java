package com.portfolio_dev;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class PortfolioScheduler {

    @Scheduled(cron = "0 0 0 * * *") // Runs every night at midnight
    public void cleanOldLogs() {
        System.out.println("Cleaning up temporary data...");
    }
}