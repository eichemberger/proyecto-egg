package com.proyectoegg.libros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LibrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrosApplication.class, args);
	}

}
