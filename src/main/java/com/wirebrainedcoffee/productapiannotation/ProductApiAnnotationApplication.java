package com.wirebrainedcoffee.productapiannotation;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.wirebrainedcoffee.productapiannotation.model.Product;
import com.wirebrainedcoffee.productapiannotation.repository.ProductRepository;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApiAnnotationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiAnnotationApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			Flux<Product> productFlux = Flux.fromIterable(Arrays.asList(new Product(null, "Big Latte", 2.99),
					new Product(null, "Big Decaf", 2.49), new Product(null, "Green Tea", 1.99))).flatMap(repository::save);
			
			productFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		};
	}

}
