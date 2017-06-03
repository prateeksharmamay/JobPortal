package com.apply.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableJpaRepositories(basePackages = "com.apply.repositories")
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.apply.entities"})
@ComponentScan(basePackages = {"com.apply.entities","com.apply.controllers","com.apply.aspect","com.apply.repositories","com.apply.services","com.apply.aws"})
@SpringBootApplication
public class JobPortalApplication extends WebMvcConfigurerAdapter {
	public static void main(String[] args) {
		SpringApplication.run(JobPortalApplication.class, args);
	}
}