package org.anil.testcontainers.test1.repository;

import org.anil.testcontainers.test1.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository  extends JpaRepository<OrderEntity,Long> {
}