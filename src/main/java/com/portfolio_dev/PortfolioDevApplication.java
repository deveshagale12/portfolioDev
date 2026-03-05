package com.portfolio_dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling; // Recommended
@SpringBootApplication
@EnableAsync
public class PortfolioDevApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioDevApplication.class, args);
	}

}
