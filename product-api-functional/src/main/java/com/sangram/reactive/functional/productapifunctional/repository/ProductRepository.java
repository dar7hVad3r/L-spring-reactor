package com.sangram.reactive.functional.productapifunctional.repository;

import com.sangram.reactive.functional.productapifunctional.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
