package com.wirebrainedcoffee.productapiannotation.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import com.wirebrainedcoffee.productapiannotation.model.Product;

@Component
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
