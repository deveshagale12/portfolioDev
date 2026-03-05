package com.portfolio_dev;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }
    
    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 1. Core threads: Always kept alive (e.g., 2 emails at once)
        executor.setCorePoolSize(2);
        
        // 2. Max threads: Maximum allowed if the queue is full (e.g., 5 total)
        executor.setMaxPoolSize(5);
        
        // 3. Queue capacity: How many emails can wait in line (e.g., 100)
        executor.setQueueCapacity(100);
        
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }
}