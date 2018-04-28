package com.keypr.overbooking;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OverbookingApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OverbookingApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.setLogStartupInfo(false);
		app.run(args);
	}


}
