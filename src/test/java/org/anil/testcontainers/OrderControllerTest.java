package org.anil.testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.anil.testcontainers.test1.entity.OrderEntity;
import org.anil.testcontainers.test1.repository.OrderEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class OrderControllerTest {

    static final MySQLContainer MY_SQL_CONTAINER;


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    private OrderEntityRepository  orderEntityRepository;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest");
        MY_SQL_CONTAINER.withUsername("root")
                        .withDatabaseName("containers")
                        .withPassword("root1234")
                        .withInitScript("init.sql");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",() -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username",() -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password",() -> MY_SQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");
    }

    @BeforeEach
    public void beforeEach(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderType(1L);
        orderEntity.setOrderName("order-1");
        orderEntityRepository.save(orderEntity);
    }
    @AfterEach
    public void afterEach(){
        orderEntityRepository.deleteAll();
    }

    @PersistenceContext
    EntityManager entityManager;

    @Test
    void saveOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderType(1L);
        orderEntity.setOrderName("order-2");
        webTestClient.post()
                .uri("/save")
                .bodyValue(orderEntity)
                .exchange()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(OrderEntity.class)
                .consumeWith(orderentity -> Assertions.assertNotNull(orderentity.getResponseBody().getId()));

       var abc =  entityManager.createNativeQuery("select e.name from todo1.employee e");
       var cba = abc.getResultList();
    }

    @Test
    void getOrderEntity() {
        webTestClient.get()
                .uri("/allorders")
                .exchange()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(OrderEntity.class)
                .consumeWith(listOfObject ->{
                   var list  = listOfObject.getResponseBody();
                    Assertions.assertTrue(list.size() == 1);
                    Assertions.assertTrue(list.get(0).getOrderTypeDes().equals("FullFilled"));
                });
    }
}