package com.ohmea.todayrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodayrecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayrecipeApplication.class, args);
	}

}
