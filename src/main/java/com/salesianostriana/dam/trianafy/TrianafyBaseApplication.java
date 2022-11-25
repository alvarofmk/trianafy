package com.salesianostriana.dam.trianafy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(description = "Api de gestión de música con spring Rest, JPA e hibernate",
		version = "1.0",
		contact = @Contact(email = "alvarofmk@gmail.com", name = "Álvaro Franco Martínez"),
		title = "API trianafy"
)
)
public class TrianafyBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrianafyBaseApplication.class, args);
	}

}
