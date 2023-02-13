package com.sangram.reactive.productapi;

import com.sangram.reactive.productapi.model.Product;
import com.sangram.reactive.productapi.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			var productFlux = Flux.just(
					new Product(null, "Big Latte", "50"),
					new Product(null, "Esspresso", "75"),
					new Product(null, "Cappucino", "100")
			).flatMap(repository::save);
			productFlux
					.thenMany(repository.findAll())
					.subscribe(System.out::println);
		};
	}
}
