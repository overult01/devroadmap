package dev.road.map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EntityScan(basePackages = "dev.road.map.domain")
@ComponentScan(basePackages = { "dev.road.map.*" })
@SpringBootApplication
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}