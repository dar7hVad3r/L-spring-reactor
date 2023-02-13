package com.sangram.reactive.productapi.repository;

import com.sangram.reactive.productapi.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
