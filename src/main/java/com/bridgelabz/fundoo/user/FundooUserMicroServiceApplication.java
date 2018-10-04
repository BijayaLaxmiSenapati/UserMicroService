package com.bridgelabz.fundoo.user;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class FundooUserMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundooUserMicroServiceApplication.class, args);
	}
	
	
	@Bean
	public SearchSourceBuilder searchSourceBuilder() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.from(0);
		searchSourceBuilder.size(10);
		return searchSourceBuilder;
		
	}
}
