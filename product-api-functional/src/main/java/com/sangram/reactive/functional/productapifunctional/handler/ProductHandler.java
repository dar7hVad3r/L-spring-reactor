package com.sangram.reactive.functional.productapifunctional.handler;

import com.sangram.reactive.functional.productapifunctional.model.Product;
import com.sangram.reactive.functional.productapifunctional.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
public class ProductHandler {

  private final ProductRepository repository;

  public ProductHandler(ProductRepository repository) {
    this.repository = repository;
  }

  public Mono<ServerResponse> getAll(ServerRequest request) {
    var products = repository.findAll();

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(products, Product.class);
  }

  public Mono<ServerResponse> getById(ServerRequest request) {
    String id = request.pathVariable("id");
    return repository.findById(id)
        .flatMap(p ->
          ServerResponse.ok()
              .contentType(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(p))
        )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> save(ServerRequest request) {
    return request.bodyToMono(Product.class)
        .flatMap(product ->
            ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(repository.save(product)))
        );
  }

  public Mono<ServerResponse> update(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<Product> productRequestMono = request.bodyToMono(Product.class);
    Mono<Product> productMono = repository.findById(id);

    return productRequestMono.zipWith(productMono, (newProduct, existingProduct) ->
        new Product(existingProduct.getId(), newProduct.getName(), newProduct.getPrice()))
        .flatMap(product ->
              ServerResponse.status(HttpStatus.OK)
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(BodyInserters.fromValue(repository.save(product)))
            )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> event(ServerRequest request) {
    var eventFlux =  Flux.interval(Duration.ofSeconds(1))
        .map(val -> Map.of("event", String.valueOf(val)));

    return ServerResponse.status(HttpStatus.OK)
              .contentType(MediaType.TEXT_EVENT_STREAM)
              .body(BodyInserters.fromValue(eventFlux));
  }

  public Mono<ServerResponse> delete(ServerRequest request) {
    String id = request.pathVariable("id");
    return repository.findById(id)
        .flatMap(product ->
              ServerResponse.status(HttpStatus.OK)
                  .build(repository.delete(product))
        ).switchIfEmpty(ServerResponse.notFound().build());
  }

}
