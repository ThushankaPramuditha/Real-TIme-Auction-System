package com.example.Real_time.Auction.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealTimeAuctionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeAuctionSystemApplication.class, args);
	}

}
