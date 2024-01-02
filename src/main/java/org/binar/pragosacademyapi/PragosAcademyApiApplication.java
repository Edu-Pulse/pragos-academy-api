package org.binar.pragosacademyapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		servers = {
				@Server(url = "https://pragosacademy.et.r.appspot.com/")
		}
)
public class PragosAcademyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PragosAcademyApiApplication.class, args);
	}

}
