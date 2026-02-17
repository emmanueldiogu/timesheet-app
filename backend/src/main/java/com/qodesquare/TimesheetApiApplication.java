package com.qodesquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TimesheetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetApiApplication.class, args);
		System.out.println("🚀 Timesheet API v0.0.1-SNAPSHOT Ready!");
	}

}
