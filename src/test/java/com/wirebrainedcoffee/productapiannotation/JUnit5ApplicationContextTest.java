package com.wirebrainedcoffee.productapiannotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.wirebrainedcoffee.productapiannotation.model.Product;
import com.wirebrainedcoffee.productapiannotation.model.ProductEvent;
import com.wirebrainedcoffee.productapiannotation.repository.ProductRepository;

import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JUnit5ApplicationContextTest {
	private WebTestClient client;

	private List<Product> expectedList;

	@Autowired
	private ProductRepository repository;

	@Autowired
	private ApplicationContext context;

	@BeforeEach
	public void beforeEach() {
		this.client = WebTestClient.bindToApplicationContext(context).configureClient().baseUrl("/products").build();
		this.expectedList = repository.findAll().collectList().block();
	}

	@Test
	public void testGetAllProducts() {
		this.client.get().uri("/").exchange().expectStatus().isOk().expectBodyList(Product.class)
				.isEqualTo(this.expectedList);
	}

	@Test
	public void testProductInvalidIdNotFound() {
		client.get().uri("/aaa").exchange().expectStatus().isNotFound();
	}

	@Test
	public void testProductIdFound() {
		Product expectedProduct = expectedList.get(0);
		client.get().uri("/{id}", expectedProduct.getId()).exchange().expectStatus().isOk().expectBody(Product.class)
				.isEqualTo(expectedProduct);
	}

	@Test
	public void testProductEvents() {
		ProductEvent expectedEvent = new ProductEvent(0L, "Product Event " + 0L);
		FluxExchangeResult<ProductEvent> result = client.get().uri("/events").accept(MediaType.TEXT_EVENT_STREAM)
				.exchange().expectStatus().isOk().returnResult(ProductEvent.class);
		StepVerifier.create(result.getResponseBody()).expectNext(expectedEvent).expectNextCount(2)
				.consumeNextWith(event -> assertEquals(Long.valueOf(3), event.getEventId())).thenCancel().verify();
	}
}
