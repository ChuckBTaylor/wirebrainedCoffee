package com.wirebrainedcoffee.productapiannotation.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.wirebrainedcoffee.productapiannotation.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

}
