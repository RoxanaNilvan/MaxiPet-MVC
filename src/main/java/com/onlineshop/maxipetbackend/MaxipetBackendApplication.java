package com.onlineshop.maxipetbackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
//@ComponentScan(basePackages = {"com.onlineshop.maxipetbackend.invoice"})
public class MaxipetBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(MaxipetBackendApplication.class, args);
	}

}
