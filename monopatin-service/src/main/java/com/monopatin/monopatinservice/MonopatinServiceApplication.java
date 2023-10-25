package com.monopatin.monopatinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableMongoRepositories(basePackages = "com.monopatin.monopatinservice.Config")
public class MonopatinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonopatinServiceApplication.class, args);
	}

}
