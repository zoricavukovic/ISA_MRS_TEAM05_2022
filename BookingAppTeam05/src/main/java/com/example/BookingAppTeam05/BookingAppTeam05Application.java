package com.example.BookingAppTeam05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookingAppTeam05Application {

	public static void main(String[] args) {
		SpringApplication.run(BookingAppTeam05Application.class, args);
	}

}
