package com.sangram.reactive.functional.productapifunctional;

import com.sangram.reactive.functional.productapifunctional.handler.ProductHandler;
import com.sangram.reactive.functional.productapifunctional.model.Product;
import com.sangram.reactive.functional.productapifunctional.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ProductApiFunctionalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiFunctionalApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			var productFlux = Flux.just(
					new Product(null, "Big Latte", "50"),
					new Product(null, "Esspresso", "75"),
					new Product(null, "Cappucino", "100")
			).flatMap(repository::save);
			productFlux.thenMany(repository.findAll())
					.subscribe(System.out::println);
		};
	}

	@Bean
	RouterFunction<ServerResponse> router(ProductHandler handler) {
		return route()
				.path("products", builder -> builder.nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON).or(accept(TEXT_EVENT_STREAM))),
							nestedBuilder -> nestedBuilder
									.GET("/event", handler::event))
							.GET("/{id}", handler::getById)
							.GET(handler::getAll)
							.POST(handler::save)
							.PUT("/{id}", handler::update)
				).DELETE("/{id}", handler::delete).build();
	}

}
