package com.sangram.reactive.productapi.controller;

import com.sangram.reactive.productapi.model.Product;
import com.sangram.reactive.productapi.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

  private ProductRepository productRepository;

  public ProductController(ProductRepository repository) {
    this.productRepository = repository;
  }

  @GetMapping
  public Flux<Product> getAll() {
    return productRepository.findAll();
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<Product>> getById(@PathVariable String id) {
    return productRepository.findById(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Product> save(@RequestBody Product product) {
    return productRepository.save(product);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Product>> update(@PathVariable String id, @RequestBody Product product) {
    return productRepository.findById(id)
        .flatMap(pr -> {
          pr.setName(product.getName());
          pr.setPrice(product.getPrice());
          return productRepository.save(pr);
        })
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return productRepository.findById(id)
        .flatMap(product ->
          productRepository.delete(product)
              .then(Mono.just(ResponseEntity.ok().<Void>build()))
        );
  }

  @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Map<String, String>> event() {
    return Flux.interval(Duration.ofSeconds(1))
        .map(val -> Map.of("event", String.valueOf(val)));
  }

}
