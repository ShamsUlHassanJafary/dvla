package com.teknikality.DvlaMicroservices;


import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class DvlaMicroservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvlaMicroservicesApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration() {
		
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/dvla/**"))
				.apis(RequestHandlerSelectors.basePackage("com.teknikality.DvlaMicroservices.controller"))
				.build()
				.apiInfo(apiDetails());
	}
	

	private ApiInfo apiDetails() {
		
		return new ApiInfo(
				"Dvla Api",
				"Dvla Api for retriving data from Dvla",
				"1.0",
				"Free to Use",
				new springfox.documentation.service.Contact("Mr Mao", "http://www.carconnect.uk/", "mao@gmail.com"),
				"API Licence",
				"http://www.carconnect.uk/",
				Collections.emptyList());
	}
}
